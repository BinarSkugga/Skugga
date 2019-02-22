package com.binarskugga.skuggahttps.api;

import java.lang.reflect.Parameter;

@FunctionalInterface
public interface ParameterParser<T> {

	T parse(Parameter parameter, String argument);

}
