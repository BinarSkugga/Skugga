package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class TooManyRequestsException extends HttpException {

	public TooManyRequestsException() {
		super(HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS.getCaption());
	}

	public TooManyRequestsException(String message) {
		super(HttpStatus.TOO_MANY_REQUESTS, message);
	}

}
