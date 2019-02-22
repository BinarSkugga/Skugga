package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;

import java.lang.reflect.Parameter;

public class StringArrayParser implements ParameterParser<String[]> {

	@Override
	public String[] parse(Parameter parameter, String argument) {
		return argument.split(",");
	}

}
