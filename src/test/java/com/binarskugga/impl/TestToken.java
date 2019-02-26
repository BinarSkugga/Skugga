package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.annotation.Token;
import com.binarskugga.skuggahttps.api.impl.BasicToken;

import java.util.*;
import java.util.concurrent.*;

@Token
public class TestToken extends BasicToken {

	private static final long LTT_EXPIRES = new Date().getTime() + TimeUnit.DAYS.toMillis(365);
	private static final long STT_EXPIRES = new Date().getTime() + TimeUnit.HOURS.toMillis(2);

	public TestToken() {
		setExpires(STT_EXPIRES);
	}

	public void setLTT(boolean ltt) {
		super.setLTT(ltt);
		if(ltt) this.setExpires(LTT_EXPIRES);
		else this.setExpires(STT_EXPIRES);
	}

}