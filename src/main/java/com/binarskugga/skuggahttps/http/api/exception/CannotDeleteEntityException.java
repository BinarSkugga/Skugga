package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;

import static com.binarskugga.skuggahttps.response.Response.*;

public class CannotDeleteEntityException extends APIException {

	public CannotDeleteEntityException() {
		super(BAD_REQUEST, "E_CANT_DELETE_ENTITY", "Cannot delete this entity.");
	}

	public CannotDeleteEntityException(Class specific) {
		super(BAD_REQUEST, "E_CANT_DELETE_ENTITY", "Cannot delete this entity.");
		setName("E_CANT_DELETE_" + specific.getTypeName().toUpperCase());
	}
}
