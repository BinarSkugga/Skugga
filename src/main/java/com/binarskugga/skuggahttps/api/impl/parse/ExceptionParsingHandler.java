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
		if(parsers == null) parsers = new ArrayList<>();
	}

	@Override
	public List<ExceptionParser> getParsers() {
		return parsers;
	}

}
