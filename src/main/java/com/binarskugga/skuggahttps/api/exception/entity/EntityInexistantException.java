package com.binarskugga.skuggahttps.api.exception.entity;

import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class EntityInexistantException extends BadRequestException {

	public EntityInexistantException() {
		super("Entity doesn't exists.");
	}

	public EntityInexistantException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " doesn't exists.");
	}

}
