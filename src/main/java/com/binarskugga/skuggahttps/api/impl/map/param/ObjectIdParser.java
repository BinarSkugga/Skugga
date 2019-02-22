package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;
import org.bson.types.ObjectId;

import java.lang.reflect.Parameter;

public class ObjectIdParser implements ParameterParser<ObjectId> {

	@Override
	public ObjectId parse(Parameter parameter, String argument) {
		return new ObjectId(argument);
	}

}
