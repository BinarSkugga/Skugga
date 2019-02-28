package com.binarskugga.skugga.api.exception.auth;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class InsufficientRoleException extends BadRequestException {

	public InsufficientRoleException() {
		super("User has an insufficient role to access this resource.");
	}

}
