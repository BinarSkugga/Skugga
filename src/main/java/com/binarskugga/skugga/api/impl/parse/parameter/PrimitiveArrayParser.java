package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.conversion.PrimitivaConversion;
import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class PrimitiveArrayParser implements ParameterParser<Object, String> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		return PrimitivaConversion.array(String.class).setSeparator(",").convertTo(parameter.getType(), argument);
	}

	@Override
	public boolean predicate(Parameter c) {
		return PrimitivaReflection.isPrimitiveArrayOrBoxed(c.getType());
	}

}
