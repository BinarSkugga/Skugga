package com.binarskugga.skuggahttps.http.api.filter.impl;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.exception.*;
import com.binarskugga.skuggahttps.http.api.filter.*;
import com.binarskugga.skuggahttps.validation.*;

import java.util.*;

@Filter(-1)
public class BodyValidationFilter extends PreFilter {
	@Override
	public boolean apply(HttpSession httpSession) {
		if(!(httpSession.getBody() instanceof ForeignInput)) return true;

		ForeignInput body = (ForeignInput) httpSession.getBody();
		Set<ValidationError> errors = body.validate();
		if(errors.size() > 0) {
			throw new ValidationException(body, errors);
		}

		return true;
	}
}
