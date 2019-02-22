package com.binarskugga.skuggahttps.util;

public class EndpointUtils {

	private EndpointUtils() {}

	public static String sanitizePath(String path) {
		if(path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}

}
