package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class NotAcceptableException extends HttpException {

	public NotAcceptableException() {
		super(HttpStatus.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE.getCaption());
	}

	public NotAcceptableException(String message) {
		super(HttpStatus.NOT_ACCEPTABLE, message);
	}

}
