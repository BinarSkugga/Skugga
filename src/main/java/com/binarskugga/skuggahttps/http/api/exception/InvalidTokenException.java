package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;

public class InvalidTokenException extends APIException {

	public InvalidTokenException() {
		super(Response.BAD_REQUEST, "E_INVALID_TOKEN", "The token provided is invalid.");
	}
}
