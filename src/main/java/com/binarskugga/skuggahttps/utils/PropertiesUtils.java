package com.binarskugga.skuggahttps.utils;

public class PropertiesUtils {

	private PropertiesUtils() { }

	public static Integer parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch(Exception e) {
			return null;
		}
	}

	public static Float parseFloat(String value) {
		try {
			return Float.parseFloat(value);
		} catch(Exception e) {
			return null;
		}
	}

	public static Double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch(Exception e) {
			return null;
		}
	}

	public static Boolean parseBoolean(String value) {
		try {
			return Boolean.parseBoolean(value);
		} catch(Exception e) {
			return null;
		}
	}

}
