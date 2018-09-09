package com.binarskugga.skuggahttps.http;

import lombok.*;

@ToString
public class Response {

	public static final int OK = 200;
	public static final int CREATED = 201;
	public static final int ACCEPTED = 202;
	public static final int NO_CONTENT = 204;

	public static final int MOVED_PERM = 301;
	public static final int SEE_OTHER = 303;
	public static final int NOT_MODIFIED = 304;
	public static final int TEMP_REDIRECT = 307;
	public static final int PERM_REDIRECT = 308;

	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int PAYMENT_REQUIRED = 402;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int METHOD_NOT_ALLOWED = 405;
	public static final int NOT_ACCEPTABLE = 406;
	public static final int REQUEST_TIMEOUT = 408;
	public static final int TOO_MANY_REQUESTS = 429;

	public static final int INTERNAL_ERROR = 500;
	public static final int NOT_IMPLEMENTED = 501;
	public static final int BAD_GATEWAY = 502;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int HTTP_VERSION_UNSUPPORTED = 505;

	@Getter private int status;
	@Getter @Setter private String body;
	@Getter private String name;

	private Response(int status, String body) {
		this.status = status;
		this.body = body;
	}

	private Response(int status, String name, String body) {
		this.status = status;
		this.name = name;
		this.body = body;
	}

	public static Response create(int status, String body) {
		return new Response(status, body) {};
	}

	public static Response create(int status, String name, String body) {
		return new Response(status, name, body) {};
	}

	public static Response internalError(String body) {
		return new Response(INTERNAL_ERROR, body) {};
	}

	public static Response internalError() {
		return new Response(INTERNAL_ERROR, null) {};
	}

	public static Response ok(String body) {
		return new Response(OK, body) {};
	}

	public static Response ok() {
		return new Response(OK, null) {};
	}

	public static Response created(String body) {
		return new Response(CREATED, body) {};
	}

	public static Response created() {
		return new Response(CREATED, null) {};
	}

	public static Response accepted(String body) {
		return new Response(ACCEPTED, body) {};
	}

	public static Response accepted() {
		return new Response(ACCEPTED, null) {};
	}

	public static Response bad(String body) {
		return new Response(BAD_REQUEST, body) {};
	}

	public static Response bad() {
		return new Response(BAD_REQUEST, null) {};
	}

	public static Response unauthorized(String body) {
		return new Response(UNAUTHORIZED, body) {};
	}

	public static Response unauthorized() {
		return new Response(UNAUTHORIZED, null) {};
	}

	public static Response forbidden(String body) {
		return new Response(FORBIDDEN, body) {};
	}

	public static Response forbidden() {
		return new Response(FORBIDDEN, null) {};
	}

	public static Response notfound(String body) {
		return new Response(NOT_FOUND, body) {};
	}

	public static Response notfound() {
		return new Response(NOT_FOUND, null) {};
	}
}
