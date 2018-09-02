package com.binarskugga.skuggahttps.utils.security.cypher;

public class Base64 {

	private Base64() {}

	public static byte[] decode(byte[] data) {
		return java.util.Base64.getDecoder().decode(data);
	}

	public static byte[] encode(byte[] data) {
		return java.util.Base64.getEncoder().encode(data);
	}

	public static String decode(String data) {
		return new String(java.util.Base64.getDecoder().decode(data));
	}

	public static String encode(String data) {
		return new String(java.util.Base64.getEncoder().encode(data.getBytes()));
	}

}
