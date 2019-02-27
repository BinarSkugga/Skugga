package com.binarskugga.skuggahttps.api.exception.entity;

import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class EntityNotLoadableException extends BadRequestException {

	public EntityNotLoadableException(Class<? extends BaseEntity> entity) {
		super("BaseEntity of type " + entity.getName() + " cannot be loaded.");
	}

}
