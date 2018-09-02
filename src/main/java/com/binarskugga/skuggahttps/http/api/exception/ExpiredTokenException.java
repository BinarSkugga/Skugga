package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;

public class ExpiredTokenException extends APIException {

	public ExpiredTokenException() {
		super(Response.BAD_REQUEST, "E_EXPIRED_TOKEN", "The token provided has expired.");
	}
}
