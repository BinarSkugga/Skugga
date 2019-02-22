package com.binarskugga.skuggahttps.api.impl.token;

import java.util.UUID;

public class UUIDToken extends BasicToken<UUID> {

	@Override
	public String getStringAuthentifier() {
		return this.getAuthentifier().toString();
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(UUID.fromString(authentifier));
	}

}
