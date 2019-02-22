package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Parameter;

public class ClassParser implements ParameterParser<Class> {

	@Override
	public Class parse(Parameter parameter, String argument) {
		return ReflectionUtils.forNameOrNull(argument);
	}

}
