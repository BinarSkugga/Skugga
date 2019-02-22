package com.binarskugga.impl;

import com.squareup.moshi.Moshi;
import lombok.Synchronized;

public class MoshiProvider {

	private static Moshi instance;

	private MoshiProvider() {}

	@Synchronized
	public static Moshi get() {
		if(instance == null) {
			instance = new Moshi.Builder()
					.build();
		}

		return instance;
	}

}
