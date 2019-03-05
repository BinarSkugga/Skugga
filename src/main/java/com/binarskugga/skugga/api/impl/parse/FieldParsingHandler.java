package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.FieldParser;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.List;

public class FieldParsingHandler extends ParsingHandler<FieldParser, Field> {

	private static FieldParsingHandler instance;

	@Accessors(chain = false)
	@Getter @Setter private List<FieldParser> parsers;

	private FieldParsingHandler() {
		super(FieldParser.class);
	}

	@Synchronized
	public static FieldParsingHandler get() {
		if(instance == null)
			instance = new FieldParsingHandler();

		return instance;
	}

}
