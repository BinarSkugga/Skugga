package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.ExceptionParser;
import com.binarskugga.skugga.api.exception.NoExceptionParserException;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;

import java.util.ArrayList;
import java.util.List;

public class ExceptionParsingHandler extends ParsingHandler<ExceptionParser, Endpoint> {

	private static List<ExceptionParser> parsers;

	private ExceptionParsingHandler() {
		super(ExceptionParser.class);
	}

	public static ExceptionParsingHandler get() {
		return new ExceptionParsingHandler();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init(ExceptionParser.class, parsers);
		if(parsers.size() == 0) throw new NoExceptionParserException();
	}

	@Override
	public List<ExceptionParser> getParsers() {
		return parsers;
	}

}
