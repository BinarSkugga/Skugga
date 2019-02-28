package com.binarskugga.skugga.api.exception.entity;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class EntityNotCreatableException extends BadRequestException {

	public EntityNotCreatableException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " cannot be created.");
	}

}
