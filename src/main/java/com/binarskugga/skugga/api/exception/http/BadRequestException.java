package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class BadRequestException extends HttpException {

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}

	public BadRequestException() {
		super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getCaption());
	}

}
