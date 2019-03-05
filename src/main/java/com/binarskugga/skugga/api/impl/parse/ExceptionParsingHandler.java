package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.ExceptionParser;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;

import java.util.List;

public class ExceptionParsingHandler extends ParsingHandler<ExceptionParser, Endpoint> {

	private static ExceptionParsingHandler instance;

	@Accessors(chain = false)
	@Getter @Setter private List<ExceptionParser> parsers;

	private ExceptionParsingHandler() {
		super(ExceptionParser.class);
	}

	@Synchronized
	public static ExceptionParsingHandler get() {
		if(instance == null)
			instance = new ExceptionParsingHandler();

		return instance;
	}

}
