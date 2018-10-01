package com.binarskugga.skuggahttps.http.api.exception;

import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;
import com.binarskugga.skuggahttps.validation.*;

import java.util.*;

public class ValidationException extends APIException {

	public ValidationException(Set<ValidationError> errors) {
		super(Response.BAD_REQUEST, "E_VALIDATION", "One or multiple inputs are invalid.");
		setErrors(errors);
	}
}
