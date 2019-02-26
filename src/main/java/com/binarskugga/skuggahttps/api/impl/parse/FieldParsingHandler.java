package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.FieldParser;

import java.lang.reflect.Field;
import java.util.*;

public class FieldParsingHandler extends ParsingHandler<FieldParser, Field> {

	private static List<FieldParser> parsers;

	public FieldParsingHandler() {
		super(FieldParser.class);
		if(parsers == null) parsers = new ArrayList<>();
	}

	public static void init() {
		parsers = new ArrayList<>();
		ParsingHandler.init("com.binarskugga.skuggahttps.api.impl.parse.field", FieldParser.class, parsers);
	}

	@Override
	public List<FieldParser> getParsers() {
		return parsers;
	}

}
