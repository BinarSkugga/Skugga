package com.binarskugga.skugga.api.exception.entity;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class EntityNotObtainableException extends BadRequestException {

	public EntityNotObtainableException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " cannot be loaded.");
	}

}
