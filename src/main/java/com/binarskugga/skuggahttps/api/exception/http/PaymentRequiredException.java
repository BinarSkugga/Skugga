package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class PaymentRequiredException extends HttpException {

	public PaymentRequiredException() {
		super(HttpStatus.PAYMENT_REQUIRED, HttpStatus.PAYMENT_REQUIRED.getCaption());
	}

	public PaymentRequiredException(String message) {
		super(HttpStatus.PAYMENT_REQUIRED, message);
	}

}
