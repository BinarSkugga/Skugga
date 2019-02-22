package com.binarskugga.skuggahttps.api.impl.token;

public class LongToken extends BasicToken<Long> {

	@Override
	public String getStringAuthentifier() {
		return String.valueOf(this.getAuthentifier());
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(Long.parseLong(authentifier));
	}

}
