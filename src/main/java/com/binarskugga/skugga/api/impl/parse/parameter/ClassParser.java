package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class ClassParser implements ParameterParser<Class, String> {

	@Override
	public Class parse(Parameter parameter, String argument) {
		return PrimitivaReflection.forNameOrNull(argument);
	}

	@Override
	public boolean predicate(Parameter c) {
		return Class.class.equals(c.getType());
	}

}
