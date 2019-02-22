package com.binarskugga.skuggahttps.util;

import java.io.File;
import java.io.InputStream;

public class ResourceUtils {

	private ResourceUtils() {}

	public static File getResourceFile(String name) {
		File file = new File("src/main/java/resources/" + name);
		return file;
	}

	public static File getTestResourceFile(String name) {
		File file = new File("src/test/java/resources/" + name);
		return file;
	}

	public static InputStream getCompiledResource(String name) {
		return ClassLoader.getSystemResourceAsStream(name);
	}

}
