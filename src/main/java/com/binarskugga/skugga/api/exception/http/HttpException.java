package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;
import lombok.Getter;

public abstract class HttpException extends RuntimeException {

	@Getter
	private HttpStatus status;

	public HttpException(HttpStatus status) {
		this.status = status;
	}

	public HttpException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

}
