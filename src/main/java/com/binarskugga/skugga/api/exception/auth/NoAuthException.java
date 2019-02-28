package com.binarskugga.skugga.api.exception.auth;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class NoAuthException extends BadRequestException {

	public NoAuthException() {
		super("No authentication has been set for this server.");
	}

}
