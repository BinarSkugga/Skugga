package com.binarskugga.skuggahttps.api.exception.auth;

import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class InvalidTokenException extends BadRequestException {

	public InvalidTokenException() {
		super("This token is invalid. It has been compromised or has expired.");
	}

}
