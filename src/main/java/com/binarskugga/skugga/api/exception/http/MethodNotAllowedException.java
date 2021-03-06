package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class MethodNotAllowedException extends HttpException {

	public MethodNotAllowedException() {
		super(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getCaption());
	}

	public MethodNotAllowedException(String message) {
		super(HttpStatus.METHOD_NOT_ALLOWED, message);
	}

}
