package com.binarskugga.skuggahttps.parse;

public abstract class URLParamParser<T> {
	public abstract T parse(String value);

	@Override
	public int hashCode() {
		return 0;
	}
}
