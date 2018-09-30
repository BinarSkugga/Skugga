package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;

public class InvalidLoginException extends APIException {

	public InvalidLoginException() {
		super(Response.BAD_REQUEST, "E_INVALID_LOGIN", "The username and password don't match.");
	}
}
