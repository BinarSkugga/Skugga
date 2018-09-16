package com.binarskugga.skuggahttps.http.api;

import com.binarskugga.skuggahttps.validation.*;
import lombok.*;

import java.util.*;

public class APIException extends RuntimeException {

	@Getter private int status;
	@Getter @Setter private String name;

	@Getter @Setter private Object body;
	@Getter @Setter private Set<ValidationError> errors;

	public APIException(int status, String name, String message) {
		super(message);
		this.status = status;
		this.name = name;
	}
}
