package io.undertow.util;

public class HeaderValuesFactory {

	private HeaderValuesFactory() {}

	public static HeaderValues create(String key) {
		return new HeaderValues(new HttpString(key));
	}

	public static HeaderValues create(String key, Object value) {
		HeaderValues hv = new HeaderValues(new HttpString(key));
		hv.add(String.valueOf(value));
		return hv;
	}

}
