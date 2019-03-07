package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.conversion.PrimitivaConversion;
import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class PrimitiveParser implements ParameterParser<Object> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		return PrimitivaConversion.single(String.class).convertTo(parameter.getType(), argument);
	}

	@Override
	public boolean predicate(Parameter c) {
		return PrimitivaReflection.isPrimitiveOrBoxed(c.getType());
	}

}
