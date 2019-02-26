package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;

import java.util.ArrayList;
import java.util.List;

public class BodyParsingHandler extends ParsingHandler<BodyParser, Endpoint> {

	private static List<BodyParser> parsers;

	private BodyParsingHandler() {
		super(BodyParser.class);
	}

	public static BodyParsingHandler get() {
		return new BodyParsingHandler();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init(BodyParser.class, parsers);
	}

	@Override
	public List<BodyParser> getParsers() {
		return parsers;
	}

}
