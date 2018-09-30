package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;

public class EntityExistException extends APIException {

	public EntityExistException() {
		super(Response.BAD_REQUEST, "E_ENTITY_EXIST", "This entity already exists.");
	}

	public EntityExistException(Class specific) {
		super(Response.BAD_REQUEST, "E_ENTITY_EXIST", "This entity already exists.");
		setName("E_" + specific.getSimpleName().toUpperCase() + "_EXIST");
	}
}
