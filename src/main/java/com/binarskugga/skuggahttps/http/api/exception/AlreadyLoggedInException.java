package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;

public class AlreadyLoggedInException extends APIException {

	public AlreadyLoggedInException() {
		super(Response.BAD_REQUEST, "E_ALREADY_LOGGEDIN", "You already have a valid token in your cookies.");
	}
}
