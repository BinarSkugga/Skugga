package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;
import java.util.UUID;

public class UUIDParser implements ParameterParser<UUID> {

	@Override
	public UUID parse(Parameter parameter, String argument) {
		return UUID.fromString(argument);
	}

	@Override
	public boolean predicate(Parameter c) {
		return UUID.class.equals(c.getType());
	}

}
