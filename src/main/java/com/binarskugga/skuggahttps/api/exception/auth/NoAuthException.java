package com.binarskugga.skuggahttps.api.exception.auth;

import com.binarskugga.skuggahttps.api.exception.http.BadRequestException;

public class NoAuthException extends BadRequestException {

	public NoAuthException() {
		super("No authentication has been set for this server.");
	}

}
