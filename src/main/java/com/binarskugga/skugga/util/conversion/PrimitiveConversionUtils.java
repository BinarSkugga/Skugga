package com.binarskugga.skugga.util.conversion;


public class PrimitiveConversionUtils {

	private PrimitiveConversionUtils() {}

	public static PrimitiveArrayConverter array(Class inC) {
		return new PrimitiveArrayConverter(inC);
	}

	public static PrimitiveConverter single(Class inC) {
		return new PrimitiveConverter(inC);
	}

}
