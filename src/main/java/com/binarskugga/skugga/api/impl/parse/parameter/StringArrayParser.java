package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;

public class StringArrayParser implements ParameterParser<String[]> {

	@Override
	public String[] parse(Parameter parameter, String argument) {
		return argument.split(",");
	}

	@Override
	public boolean predicate(Parameter c) {
		return CharSequence[].class.equals(c.getType()) || String[].class.equals(c.getType());
	}

}
