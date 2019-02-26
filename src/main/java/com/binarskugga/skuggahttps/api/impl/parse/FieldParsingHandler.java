package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.FieldParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldParsingHandler extends ParsingHandler<FieldParser, Field> {

	private static List<FieldParser> parsers;

	private FieldParsingHandler() {
		super(FieldParser.class);
	}

	public static FieldParsingHandler get() {
		return new FieldParsingHandler();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init(FieldParser.class, parsers);
	}

	@Override
	public List<FieldParser> getParsers() {
		return parsers;
	}

}
