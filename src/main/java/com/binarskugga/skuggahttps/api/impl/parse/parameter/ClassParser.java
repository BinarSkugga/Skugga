package com.binarskugga.skuggahttps.api.impl.parse.parameter;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Parameter;

public class ClassParser implements ParameterParser<Class, String> {

	@Override
	public Class parse(Parameter parameter, String argument) {
		return ReflectionUtils.forNameOrNull(argument);
	}

	@Override
	public boolean predicate(Parameter c) {
		return Class.class.equals(c.getType());
	}

}
