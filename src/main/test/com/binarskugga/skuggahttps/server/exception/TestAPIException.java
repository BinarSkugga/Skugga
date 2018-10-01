package com.binarskugga.skuggahttps.server.exception;

import com.binarskugga.skuggahttps.http.api.*;

import static com.binarskugga.skuggahttps.response.Response.*;

public class TestAPIException extends APIException {

	public TestAPIException() {
		super(BAD_REQUEST, "E_TEST)API", "This error has no purpose like me.");
	}

}
