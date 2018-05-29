package com.binarskugga.skuggahttps.parse;

public class LongParamParser extends URLParamParser<Long> {
	@Override
	public Long parse(String value) {
		return Long.parseLong(value);
	}
}
