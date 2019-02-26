package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.util.*;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.google.auth.*;
import com.google.auth.oauth2.*;
import com.google.cloud.datastore.*;
import com.google.common.flogger.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.Entity;
import org.reflections.*;

import java.util.stream.*;

public class ObjectifyConnector implements DataConnector<Datastore> {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	private String projectId;
	private Credentials credentials;

	public ObjectifyConnector(String projectId, Credentials credentials) {
		this.projectId = projectId;
		this.credentials = credentials;
	}

	@Override
	public Datastore connect(String modelPackage) {
		Datastore store = DatastoreOptions.newBuilder()
				.setProjectId(this.projectId).setCredentials(this.credentials)
				.build().getService();
		ObjectifyService.init(new ObjectifyFactory(store));

		Reflections reflections = new Reflections(modelPackage);
		reflections.getSubTypesOf(BaseEntity.class).stream()
				.filter(c -> ReflectionUtils.getClassAnnotationOrNull(c, Entity.class) != null)
				.collect(Collectors.toList()).forEach(c -> {
					ObjectifyService.register(c);
					logger.atFine().log("Entity class " + c.getSimpleName() + " has been registered !");
				});
		return null;
	}

}
