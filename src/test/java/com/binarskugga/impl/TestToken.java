package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.impl.token.BasicToken;
import org.bson.types.*;

import java.util.*;
import java.util.concurrent.*;

public class TestToken extends BasicToken<String> {

	private static final long LTT_EXPIRES = new Date().getTime() + TimeUnit.DAYS.toMillis(365);
	private static final long STT_EXPIRES = new Date().getTime() + TimeUnit.HOURS.toMillis(2);

	public TestToken() {
		setExpires(STT_EXPIRES);
	}

	@Override
	public String getStringAuthentifier() {
		return this.getAuthentifier();
	}

	@Override
	public void setStringAuthentifier(String authentifier) {
		this.setAuthentifier(authentifier);
	}

	public void setLTT(boolean ltt) {
		if(ltt) this.setExpires(LTT_EXPIRES);
		else this.setExpires(STT_EXPIRES);
	}

}
