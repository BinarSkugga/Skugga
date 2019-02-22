package com.binarskugga.skuggahttps.api.impl.token;

public class StringToken extends BasicToken<String> {

	@Override
	public String getStringAuthentifier() {
		return this.getAuthentifier();
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(authentifier);
	}

}
