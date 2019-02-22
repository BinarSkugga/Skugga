package com.binarskugga.skuggahttps.api.impl.map.param;

import com.binarskugga.skuggahttps.api.ParameterParser;

import java.lang.reflect.Parameter;
import java.util.UUID;

public class UUIDParser implements ParameterParser<UUID> {

	@Override
	public UUID parse(Parameter parameter, String argument) {
		return UUID.fromString(argument);
	}

}
