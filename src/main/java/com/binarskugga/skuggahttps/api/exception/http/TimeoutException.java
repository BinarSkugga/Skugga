package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class TimeoutException extends HttpException {

	public TimeoutException() {
		super(HttpStatus.TIMEOUT, HttpStatus.TIMEOUT.getCaption());
	}

	public TimeoutException(String message) {
		super(HttpStatus.TIMEOUT, message);
	}

}
