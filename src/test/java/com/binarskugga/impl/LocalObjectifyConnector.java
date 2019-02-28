package com.binarskugga.impl;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.DataConnector;
import com.binarskugga.skugga.util.ReflectionUtils;
import com.google.auth.Credentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.testing.*;
import com.google.common.flogger.FluentLogger;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import org.reflections.Reflections;

import java.util.stream.Collectors;

public class LocalObjectifyConnector implements DataConnector<Datastore> {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	@Override
	public Datastore connect(String modelPackage) {
		LocalDatastoreHelper helper = LocalDatastoreHelper.create();
		Datastore store = helper.getOptions().getService();

		ObjectifyService.init(new ObjectifyFactory(store));

		Reflections reflections = new Reflections(modelPackage);
		reflections.getSubTypesOf(BaseEntity.class).stream()
				.filter(c -> ReflectionUtils.getClassAnnotationOrNull(c, Entity.class) != null)
				.collect(Collectors.toList()).forEach(c -> {
			ObjectifyService.register(c);
			logger.atFine().log("Entity class " + c.getSimpleName() + " has been registered !");
		});
		return store;
	}

}
