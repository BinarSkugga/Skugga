package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.BodyParser;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;

import java.util.List;

public class BodyParsingHandler extends ParsingHandler<BodyParser, Endpoint> {

	private static BodyParsingHandler instance;

	@Accessors(chain = false)
	@Getter @Setter private List<BodyParser> parsers;

	private BodyParsingHandler() {
		super(BodyParser.class);
	}

	@Synchronized
	public static BodyParsingHandler get() {
		if(instance == null)
			instance = new BodyParsingHandler();

		return instance;
	}

}
