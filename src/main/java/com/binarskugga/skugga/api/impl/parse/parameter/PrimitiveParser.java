package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class PrimitiveParser implements ParameterParser<Object> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		return Primitiva.Conversion.ofPrimitive(String.class).convertTo(argument, parameter.getType());
	}

	@Override
	public boolean predicate(Parameter c) {
		return ClassTools.of(c.getType()).isPrimitiveOrBoxed();
	}

}
