package com.binarskugga.skuggahttps.http.api.filter;

import com.binarskugga.skuggahttps.validation.*;

import java.util.*;

public interface ParameterValidator<T> {

	Set<ValidationError> validate(String arg, T object);

}
