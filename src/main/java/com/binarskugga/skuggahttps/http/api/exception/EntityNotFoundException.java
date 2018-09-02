package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;

public class EntityNotFoundException extends APIException {

	public EntityNotFoundException() {
		super(Response.BAD_REQUEST, "E_ENTITY_NOTFOUND", "Couldn't find this entity.");
	}

	public EntityNotFoundException(Class specific) {
		super(Response.BAD_REQUEST, "E_ENTITY_NOTFOUND", "Couldn't find this entity.");
		setName("E_" + specific.getSimpleName().toUpperCase() + "_NOTFOUND");
	}
}
