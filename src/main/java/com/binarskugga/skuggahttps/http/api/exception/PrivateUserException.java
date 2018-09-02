package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;

import static com.binarskugga.skuggahttps.http.Response.*;

public class PrivateUserException extends APIException {

	public PrivateUserException() {
		super(BAD_REQUEST, "E_PRIVATE_USER", "This user has set their profile to be private.");
	}
}
