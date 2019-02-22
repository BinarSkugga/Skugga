package com.binarskugga.skuggahttps.api.exception.http;

public class ClosedEndpointException extends BadRequestException {

	public ClosedEndpointException() {
		super("This endpoint has been closed and cannot be accessed.");
	}

}
