package com.binarskugga.skuggahttps.api.exception.auth;

import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class LoginException extends BadRequestException {

	public LoginException() {
		super("Login information are incorrect.");
	}

}
