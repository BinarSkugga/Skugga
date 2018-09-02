package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;

import static com.binarskugga.skuggahttps.http.Response.*;

public class InsufficientRoleException extends APIException {

	public InsufficientRoleException() {
		super(BAD_REQUEST, "E_INSUFFICIENT_ROLE", "User's role insufficient for this resource.");
	}
}
