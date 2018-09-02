package com.binarskugga.skuggahttps.utils.security.cypher;

import com.google.common.base.*;
import com.google.common.hash.*;

/**
 * @author BinarSkugga
 */
public class Sha256 {

	private Sha256() {}

	public static String hash(String value) {
		return Hashing.sha256().hashString(value, Charsets.UTF_8).toString();
	}

	public static byte[] hashAsBytes(String value) {
		return Hashing.sha256().hashString(value, Charsets.UTF_8).asBytes();
	}

}
