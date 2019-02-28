package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class UpdateRequiredException extends HttpException {

	public UpdateRequiredException() {
		super(HttpStatus.UPDATE_REQUIRED, HttpStatus.UPDATE_REQUIRED.getCaption());
	}

	public UpdateRequiredException(String message) {
		super(HttpStatus.UPDATE_REQUIRED, message);
	}

}
