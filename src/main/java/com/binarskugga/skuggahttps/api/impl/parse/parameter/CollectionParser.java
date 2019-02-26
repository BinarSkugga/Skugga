package com.binarskugga.skuggahttps.api.impl.parse.parameter;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;

public class CollectionParser implements ParameterParser<Collection, String> {

	@Override
	@SuppressWarnings("unchecked")
	public Collection parse(Parameter parameter, String argument) {
		Class inner = (Class) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
		if (ReflectionUtils.isBoxedPrimitive(inner))
			return ReflectionUtils.stringToPrimitiveCollection(argument, ",", inner);
		else
			return Arrays.asList(argument.split(","));
	}

	@Override
	public boolean predicate(Parameter c) {
		return Collection.class.isAssignableFrom(c.getType());
	}

}
