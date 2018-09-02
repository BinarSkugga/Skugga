package com.binarskugga.skuggahttps.utils.security.cypher;

import com.google.common.base.*;
import com.google.common.hash.*;

public class Sha512 {

	private Sha512() {}

	public static String hash(String value) {
		return Hashing.sha512().hashString(value, Charsets.UTF_8).toString();
	}

}
