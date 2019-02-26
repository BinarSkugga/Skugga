package com.binarskugga.skuggahttps.api.enums;

import lombok.Getter;

public enum HttpStatus {
	CONTINUE(false, 100, "100 Continue"),
	SWITCHING_PROTOCOLS(false, 101, "101 Switching Protocols"),
	PROCESSING(false, 102, "102 Processing"),
	EARLY_HINTS(false, 103, "103 Early Hints"),

	OK(false, 200, "200 Ok"),
	CREATED(false, 201, "201 Created"),
	ACCEPTED(false, 202, "202 Accepted"),
	NO_CONTENT(false, 204, "204 No Content"),

	BAD_REQUEST(true, 400, "400 Bad Request"),
	UNAUTHORIZED(true, 401, "401 Unauthorized"),
	PAYMENT_REQUIRED(true, 402, "402 Payment Required"),
	FORBIDDEN(true, 403, "403 Forbidden"),
	NOT_FOUND(true, 404, "404 Not found"),
	METHOD_NOT_ALLOWED(true, 405, "405 Method Not Allowed"),
	NOT_ACCEPTABLE(true, 406, "406 Not Acceptable"),
	TIMEOUT(true, 408, "408 Timeout"),
	CONFLICT(true, 409, "409 Conflict"),
	GONE(true, 410, "410 Gone"),
	TEAPOT(true, 418, "418 I'm a teapot"),
	UPDATE_REQUIRED(true, 426, "426 Update Required"),
	TOO_MANY_REQUESTS(true, 429, "429 Too Many Requests"),

	INTERNAL_ERROR(true, 500, "500 Internal Server Error"),
	NOT_IMPLEMENTED(true, 501, "501 Not Implemented"),
	UNAVAILABLE(true, 503, "503 Service Unavailable");

	@Getter private boolean error;
	@Getter private int code;
	@Getter private String caption;

	HttpStatus(boolean error, int code, String caption) {
		this.error = error;
		this.code = code;
		this.caption = caption;
	}

	public static HttpStatus fromCode(int code) {
		for(HttpStatus status: HttpStatus.values()) {
			if(status.getCode() == code) return status;
		}
		return null;
	}

}
