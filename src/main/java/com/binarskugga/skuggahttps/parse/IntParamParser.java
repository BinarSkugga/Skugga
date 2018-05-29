package com.binarskugga.skuggahttps.parse;

public class IntParamParser extends URLParamParser<Integer> {
	@Override
	public Integer parse(String value) {
		return Integer.parseInt(value);
	}
}
