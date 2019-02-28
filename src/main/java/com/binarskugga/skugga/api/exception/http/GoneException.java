package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class GoneException extends HttpException {

	public GoneException(String message) {
		super(HttpStatus.GONE, message);
	}

	public GoneException() {
		super(HttpStatus.GONE, HttpStatus.GONE.getCaption());
	}

}
