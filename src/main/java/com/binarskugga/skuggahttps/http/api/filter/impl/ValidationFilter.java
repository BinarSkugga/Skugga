package com.binarskugga.skuggahttps.http.api.filter.impl;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.annotation.Validator;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.exception.*;
import com.binarskugga.skuggahttps.http.api.filter.*;
import com.binarskugga.skuggahttps.validation.*;

import java.lang.reflect.*;
import java.util.*;

@Filter(-1)
public class ValidationFilter extends PreFilter {
	@Override
	public boolean apply(HttpSession httpSession) {
		Set<ValidationError> paramErrors = new HashSet<>();
		for(Parameter param : httpSession.getArgs().keySet()) {
			if(param.isAnnotationPresent(Validator.class)) {
				// Validate
				Validator validatorAnn = param.getAnnotation(Validator.class);
				try {
					String name = validatorAnn.name().equals("") ? param.getName() : validatorAnn.name();
					Object value = httpSession.getArgs().get(param);
					ParameterValidator validator = validatorAnn.value().newInstance();
					Set<ValidationError> errors = validator.validate(name, value);
					if(errors.size() > 0)
						paramErrors.addAll(errors);
				} catch(IllegalAccessException | InstantiationException ignored) {}
			}
		}
		if(paramErrors.size() > 0) {
			throw new ValidationException(paramErrors);
		}

		if(httpSession.getBody() == null || !ForeignInput.class.isAssignableFrom(httpSession.getBody().getClass())) return true;

		ForeignInput body = (ForeignInput) httpSession.getBody();
		Set<ValidationError> errors = body.validate();
		if(errors.size() > 0) {
			throw new ValidationException(errors);
		}

		return true;
	}
}
