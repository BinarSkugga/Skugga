package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Parameter;

public class PrimitiveArrayParser implements ParameterParser<Object> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		if(ClassTools.of(parameter.getType()).isPrimitiveOrBoxedArray()) {
			return Primitiva.Conversion.ofPrimitive(String[].class).convertTo(argument.split(","), parameter.getType());
		}

		throw new CannotMapFieldException();
	}

	@Override public String unparse(Parameter context, Object object) throws Exception {
		// TODO: Need primitiva array to collection method
//		if(ClassTools.of(context.getType()).isPrimitiveOrBoxedArray()) {
//			return Joiner.on(",").join();
//		}

		throw new CannotMapFieldException();
	}

	@Override
	public boolean predicate(Parameter c) {
		return ClassTools.of(c.getType()).isPrimitiveOrBoxedArray();
	}

}
