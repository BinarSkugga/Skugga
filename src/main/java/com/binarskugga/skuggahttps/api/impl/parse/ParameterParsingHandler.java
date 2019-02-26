package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.ParameterParser;
import lombok.Getter;

import java.lang.reflect.Parameter;
import java.util.*;

public class ParameterParsingHandler extends ParsingHandler<ParameterParser, Parameter> {

	@Getter private static List<ParameterParser> parsers;

	public ParameterParsingHandler() {
		super(ParameterParser.class);
		if(parsers == null) parsers = new ArrayList<>();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init("com.binarskugga.skuggahttps.api.impl.parse.parameter", ParameterParser.class, parsers);
	}

	@Override
	public List<ParameterParser> getParsers() {
		return parsers;
	}

}
