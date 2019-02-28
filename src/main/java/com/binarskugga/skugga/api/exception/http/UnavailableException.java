package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class UnavailableException extends HttpException {

	public UnavailableException() {
		super(HttpStatus.UNAVAILABLE, HttpStatus.UNAVAILABLE.getCaption());
	}

	public UnavailableException(String message) {
		super(HttpStatus.UNAVAILABLE, message);
	}

}
