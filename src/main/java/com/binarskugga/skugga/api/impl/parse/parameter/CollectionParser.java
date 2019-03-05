package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;

public class CollectionParser implements ParameterParser<Collection, String> {

	@Override
	@SuppressWarnings("unchecked")
	public Collection parse(Parameter parameter, String argument) {
		Class inner = (Class) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
		if (PrimitivaReflection.isBoxedPrimitive(inner))
			return PrimitivaReflection.stringToPrimitiveCollection(argument, ",", inner);
		else
			return Arrays.asList(argument.split(","));
	}

	@Override
	public boolean predicate(Parameter c) {
		return Collection.class.isAssignableFrom(c.getType());
	}

}
