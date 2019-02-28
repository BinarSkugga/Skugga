package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.BodyParser;
import com.binarskugga.skugga.api.exception.NoBodyParserException;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;

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
		if(parsers.size() == 0) throw new NoBodyParserException();
	}

	@Override
	public List<BodyParser> getParsers() {
		return parsers;
	}

}
