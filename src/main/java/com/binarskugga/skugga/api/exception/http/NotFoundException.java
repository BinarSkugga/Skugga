package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class NotFoundException extends HttpException {

	public NotFoundException() {
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getCaption());
	}

	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

}
