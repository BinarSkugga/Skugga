package com.binarskugga.skugga.api.exception.auth;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class InvalidTokenException extends BadRequestException {

	public InvalidTokenException() {
		super("This token is invalid. It has been compromised or has expired.");
	}

}
