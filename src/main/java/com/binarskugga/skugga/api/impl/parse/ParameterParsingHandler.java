package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.ParameterParser;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;

import java.lang.reflect.Parameter;
import java.util.List;

public class ParameterParsingHandler extends ParsingHandler<ParameterParser, Parameter> {

	private static ParameterParsingHandler instance;

	@Accessors(chain = false)
	@Getter @Setter private List<ParameterParser> parsers;

	private ParameterParsingHandler() {
		super(ParameterParser.class);
	}

	@Synchronized
	public static ParameterParsingHandler get() {
		if(instance == null)
		 instance = new ParameterParsingHandler();

		return instance;
	}

}
