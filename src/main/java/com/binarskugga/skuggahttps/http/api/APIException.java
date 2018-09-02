package com.binarskugga.skuggahttps.http.api;

import lombok.*;

public class APIException extends RuntimeException {

	@Getter private int status;

	@Getter @Setter private String name;

	public APIException(int status, String name, String message) {
		super(message);
		this.status = status;
		this.name = name;
	}
}
