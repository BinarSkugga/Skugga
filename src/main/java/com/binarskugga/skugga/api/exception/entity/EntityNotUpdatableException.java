package com.binarskugga.skugga.api.exception.entity;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class EntityNotUpdatableException extends BadRequestException {

	public EntityNotUpdatableException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " cannot be updated.");
	}

}
