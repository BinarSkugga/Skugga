package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class TeapotException extends HttpException {

	public TeapotException() {
		super(HttpStatus.TEAPOT, HttpStatus.TEAPOT.getCaption());
	}

	public TeapotException(String message) {
		super(HttpStatus.TEAPOT, message);
	}

}
