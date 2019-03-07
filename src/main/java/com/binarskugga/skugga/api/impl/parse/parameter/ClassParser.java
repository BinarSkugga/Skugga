package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Parameter;

public class ClassParser implements ParameterParser<Class> {

	@Override
	public Class parse(Parameter parameter, String argument) {
		Class c = PrimitivaReflection.forNameOrNull(argument);
		if(c != null)
			return PrimitivaReflection.forNameOrNull(argument);

		throw new CannotMapFieldException();
	}

	@Override public String unparse(Parameter context, Class object) throws Exception {
		return object.getName();
	}

	@Override
	public boolean predicate(Parameter c) {
		return Class.class.equals(c.getType());
	}

}
