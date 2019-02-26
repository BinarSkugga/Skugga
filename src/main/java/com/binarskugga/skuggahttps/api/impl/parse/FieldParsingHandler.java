package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.FieldParser;

import java.lang.reflect.Field;
import java.util.*;

public class FieldParsingHandler extends ParsingHandler<FieldParser, Field> {

	private static List<FieldParser> parsers;

	public FieldParsingHandler() {
		super(FieldParser.class);
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
