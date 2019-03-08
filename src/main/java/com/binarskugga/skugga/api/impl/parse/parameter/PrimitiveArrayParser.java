package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.conversion.PrimitivaConversion;
import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Parameter;

public class PrimitiveArrayParser implements ParameterParser<Object> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		if(PrimitivaReflection.isPrimitiveArrayOrBoxed(parameter.getType())) {
			return PrimitivaConversion.array(String.class).setSeparator(",").convertTo(parameter.getType(), argument);
		}

		throw new CannotMapFieldException();
	}

	@Override public String unparse(Parameter context, Object object) throws Exception {
		// TODO: Need primitiva array to collection method
//		if(PrimitivaReflection.isPrimitiveArrayOrBoxed(context.getType())) {
//			return Joiner.on(",").join(object);
//		}

		throw new CannotMapFieldException();
	}

	@Override
	public boolean predicate(Parameter c) {
		return PrimitivaReflection.isPrimitiveArrayOrBoxed(c.getType());
	}

}
