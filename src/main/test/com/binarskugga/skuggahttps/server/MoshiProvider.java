package com.binarskugga.skuggahttps.server;

import com.squareup.moshi.*;
import lombok.*;

public class MoshiProvider {

	private static Moshi instance;

	private MoshiProvider() {}

	@Synchronized
	public static Moshi get() {
		if(instance == null) {
			instance = new Moshi.Builder()
					.add(new ObjectIdMapper())
					.build();
		}
		return instance;
	}

}
