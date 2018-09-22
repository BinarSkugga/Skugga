package com.binarskugga.skuggahttps.http.exception;

public class HTTPException extends RuntimeException {

	private int status;

	public HTTPException(int status, String message) {
		super(message.equals("") ? "No Message" : "");
		this.status = status;
	}

	public HTTPException(int status) {
		super("No Message");
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}
}
