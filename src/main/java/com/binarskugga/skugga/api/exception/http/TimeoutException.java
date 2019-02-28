package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class TimeoutException extends HttpException {

	public TimeoutException() {
		super(HttpStatus.TIMEOUT, HttpStatus.TIMEOUT.getCaption());
	}

	public TimeoutException(String message) {
		super(HttpStatus.TIMEOUT, message);
	}

}
