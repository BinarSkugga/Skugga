package com.binarskugga.skugga.api.exception.entity;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class EntityInexistantException extends BadRequestException {

	public EntityInexistantException() {
		super("Entity doesn't exists.");
	}

	public EntityInexistantException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " doesn't exists.");
	}

}
