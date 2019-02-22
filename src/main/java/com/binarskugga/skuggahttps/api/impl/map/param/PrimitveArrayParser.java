package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Parameter;

public class PrimitveArrayParser implements ParameterParser<Object> {

	@Override
	public Object parse(Parameter parameter, String argument) {
		return ReflectionUtils.stringToPrimitiveArray(argument, ",", parameter.getType());
	}

}
