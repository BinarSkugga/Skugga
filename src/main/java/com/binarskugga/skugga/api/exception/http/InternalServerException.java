package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class InternalServerException extends HttpException {

	public InternalServerException() {
		super(HttpStatus.INTERNAL_ERROR, HttpStatus.INTERNAL_ERROR.getCaption());
	}

	public InternalServerException(String message) {
		super(HttpStatus.INTERNAL_ERROR, message);
	}

}
