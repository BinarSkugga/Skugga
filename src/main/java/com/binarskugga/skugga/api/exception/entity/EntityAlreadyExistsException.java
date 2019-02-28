package com.binarskugga.skugga.api.exception.entity;

import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class EntityAlreadyExistsException extends BadRequestException {

	public EntityAlreadyExistsException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " already exists.");
	}

}
