package com.binarskugga.skuggahttps.parse;

import java.util.*;

public class UUIDParamParser extends URLParamParser<UUID> {
	@Override
	public UUID parse(String value) {
		return UUID.fromString(value);
	}
}
