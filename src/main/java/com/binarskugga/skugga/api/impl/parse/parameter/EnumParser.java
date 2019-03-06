package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class EnumParser implements ParameterParser<Enum, String> {

	@SuppressWarnings("unchecked")
	@Override public Enum parse(Parameter context, String object) throws Exception {
		Class<? extends Enum> enumClass = (Class<? extends Enum>) context.getType();
		return Enum.valueOf(enumClass, object);
	}

	@Override public String unparse(Parameter context, Enum object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Parameter c) {
		return Enum.class.isAssignableFrom(c.getType());
	}

}
