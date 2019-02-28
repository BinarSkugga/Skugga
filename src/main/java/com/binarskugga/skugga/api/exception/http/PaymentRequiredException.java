package com.binarskugga.skugga.api.exception.http;

import com.binarskugga.skugga.api.enums.HttpStatus;

public class PaymentRequiredException extends HttpException {

	public PaymentRequiredException() {
		super(HttpStatus.PAYMENT_REQUIRED, HttpStatus.PAYMENT_REQUIRED.getCaption());
	}

	public PaymentRequiredException(String message) {
		super(HttpStatus.PAYMENT_REQUIRED, message);
	}

}
