package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.ParameterParser;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterParsingHandler extends ParsingHandler<ParameterParser, Parameter> {

	private static List<ParameterParser> parsers;

	private ParameterParsingHandler() {
		super(ParameterParser.class);
	}

	public static ParameterParsingHandler get() {
		return new ParameterParsingHandler();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init(ParameterParser.class, parsers);
	}

	@Override
	public List<ParameterParser> getParsers() {
		return parsers;
	}

}
