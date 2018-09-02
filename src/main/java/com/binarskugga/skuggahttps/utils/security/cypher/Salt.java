package com.binarskugga.skuggahttps.utils.security.cypher;

import java.security.*;

public class Salt {

	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!/$%?&*()_-+=[]{}";
	private static final SecureRandom RAND = new SecureRandom();

	private Salt() {}

	public static String generate(int length) {
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) {
			int index = RAND.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

}
