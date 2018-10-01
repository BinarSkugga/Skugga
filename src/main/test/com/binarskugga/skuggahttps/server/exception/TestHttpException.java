package com.binarskugga.skuggahttps.server.exception;

import com.binarskugga.skuggahttps.http.exception.*;

import static com.binarskugga.skuggahttps.response.Response.*;

public class TestHttpException extends HTTPException {

	public TestHttpException() {
		super(INTERNAL_ERROR, "Very internal indeed :)");
	}

}
