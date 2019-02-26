package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.ExceptionParser;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;

import java.util.ArrayList;
import java.util.List;

public class ExceptionParsingHandler extends ParsingHandler<ExceptionParser, Endpoint> {

	private static List<ExceptionParser> parsers;

	public ExceptionParsingHandler() {
		super(ExceptionParser.class);
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init(BodyParser.class, parsers);
	}

	@Override
	public List<ExceptionParser> getParsers() {
		return parsers;
	}

}
