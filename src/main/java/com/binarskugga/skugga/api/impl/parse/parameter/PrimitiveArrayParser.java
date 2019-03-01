package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.*;
import com.binarskugga.skugga.util.ReflectionUtils;

import java.lang.reflect.Parameter;

public class PrimitiveArrayParser implements ParameterParser<Object, String> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		return ReflectionUtils.stringToPrimitiveArray(argument, ",", parameter.getType());
	}

	@Override
	public boolean predicate(Parameter c) {
		return ReflectionUtils.isPrimitiveArrayOrBoxed(c.getType());
	}

}
