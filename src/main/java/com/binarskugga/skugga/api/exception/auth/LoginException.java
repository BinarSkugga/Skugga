package com.binarskugga.skugga.api.exception.auth;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class LoginException extends BadRequestException {

	public LoginException() {
		super("Login information are incorrect.");
	}

}
