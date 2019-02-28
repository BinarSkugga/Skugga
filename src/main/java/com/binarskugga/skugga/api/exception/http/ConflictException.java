package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class ConflictException extends HttpException {

	public ConflictException(String message) {
		super(HttpStatus.CONFLICT, message);
	}

	public ConflictException() {
		super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getCaption());
	}

}
