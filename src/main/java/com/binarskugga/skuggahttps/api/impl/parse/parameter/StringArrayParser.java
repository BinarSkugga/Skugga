package com.binarskugga.skuggahttps.api.impl.parse.parameter;

import com.binarskugga.skuggahttps.api.ParameterParser;

import java.lang.reflect.Parameter;

public class StringArrayParser implements ParameterParser<String[], String> {

	@Override
	public String[] parse(Parameter parameter, String argument) {
		return argument.split(",");
	}

	@Override
	public boolean predicate(Parameter c) {
		return CharSequence[].class.equals(c.getType()) || String[].class.equals(c.getType());
	}

}
