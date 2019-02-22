package com.binarskugga.skuggahttps.api.exception.auth;

import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class InsufficientRoleException extends BadRequestException {

	public InsufficientRoleException() {
		super("User has an insufficient role to access this resource.");
	}

}
