package com.binarskugga.skuggahttps.api.impl.parse.parameter;

import com.binarskugga.skuggahttps.api.ParameterParser;

import java.lang.reflect.Parameter;

public class StringParser implements ParameterParser<CharSequence, String> {

	@Override
	public CharSequence parse(Parameter parameter, String argument) {
		return argument;
	}

	@Override
	public boolean predicate(Parameter c) {
		return CharSequence.class.isAssignableFrom(c.getType());
	}

}
