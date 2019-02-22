package com.binarskugga.skuggahttps.api.impl.token;

public class IntegerToken extends BasicToken<Integer> {

	@Override
	public String getStringAuthentifier() {
		return String.valueOf(this.getAuthentifier());
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(Integer.parseInt(authentifier));
	}

}
