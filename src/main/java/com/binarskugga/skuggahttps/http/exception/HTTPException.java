package com.binarskugga.skuggahttps.http.exception;

public class HTTPException extends RuntimeException {

	private int status;

	public HTTPException(int status, String message) {
		super(message);
		this.status = status;
	}

	public HTTPException(int status) {
		super("");
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}
}
