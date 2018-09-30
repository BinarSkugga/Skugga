package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;

public class PasswordMismatchException extends APIException {

	public PasswordMismatchException() {
		super(Response.BAD_REQUEST, "E_PASSWORD_MISMATCH", "The two passwords provided are not identical.");
	}
}
