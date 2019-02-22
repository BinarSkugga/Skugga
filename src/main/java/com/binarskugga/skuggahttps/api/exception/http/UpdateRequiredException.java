package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class UpdateRequiredException extends HttpException {

	public UpdateRequiredException() {
		super(HttpStatus.UPDATE_REQUIRED, HttpStatus.UPDATE_REQUIRED.getCaption());
	}

	public UpdateRequiredException(String message) {
		super(HttpStatus.UPDATE_REQUIRED, message);
	}

}
