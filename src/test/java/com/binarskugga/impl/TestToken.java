package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.impl.token.BasicToken;

import java.util.UUID;

public class TestToken extends BasicToken<UUID> {

	@Override
	public String getStringAuthentifier() {
		return this.getAuthentifier().toString();
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(UUID.fromString(authentifier));
	}

}
