package com.binarskugga.skuggahttps.parse;

public class StringParamParser extends URLParamParser<String> {
	@Override
	public String parse(String value) {
		return value;
	}
}
