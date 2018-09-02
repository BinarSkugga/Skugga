package com.binarskugga.skuggahttps.utils.security.cypher;

import com.google.common.base.*;
import com.google.common.hash.*;

/**
 * @author BinarSkugga
 */
public class HSha256 {

	private HSha256() {}

	public static String hash(byte[] key, String value) {
		return Hashing.hmacSha256(key).hashString(value, Charsets.UTF_8).toString();
	}

	public static byte[] hashAsBytes(byte[] key, String value) {
		return Hashing.hmacSha256(key).hashString(value, Charsets.UTF_8).asBytes();
	}

}
