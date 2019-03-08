package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Parameter;

public class EnumParser implements ParameterParser<Enum> {

	@SuppressWarnings("unchecked")
	@Override public Enum parse(Parameter context, String object) throws Exception {
		if(Enum.class.isAssignableFrom(context.getType())) {
			return Enum.valueOf((Class<? extends Enum>) context.getType(), object);
		}

		throw new CannotMapFieldException();
	}

	@Override public String unparse(Parameter context, Enum object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Parameter c) {
		return Enum.class.isAssignableFrom(c.getType());
	}

}
