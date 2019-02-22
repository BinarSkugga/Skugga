package com.binarskugga.skuggahttps.api.exception.entity;

import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class EntityAlreadyExistsException extends BadRequestException {

	public EntityAlreadyExistsException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " already exists.");
	}

}
