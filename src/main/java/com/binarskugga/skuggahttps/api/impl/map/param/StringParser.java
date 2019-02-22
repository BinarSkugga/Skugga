package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;

import java.lang.reflect.Parameter;

public class StringParser implements ParameterParser<CharSequence> {

	@Override
	public CharSequence parse(Parameter parameter, String argument) {
		return argument;
	}

}
