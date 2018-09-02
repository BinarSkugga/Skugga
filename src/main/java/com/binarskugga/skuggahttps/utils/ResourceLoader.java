package com.binarskugga.skuggahttps.utils;

import com.google.common.io.*;

import java.io.*;

public class ResourceLoader {

	private ResourceLoader() {
	}

	public static InputStream load(String path) {
		return ResourceLoader.class.getClassLoader().getResourceAsStream(path);
	}

	public static File loadAndCreate(String path, String name) {
		try {
			File file = new File(ResourceLoader.class.getClassLoader().getResource(path).getPath(), name);
			Files.touch(file);
			return file;
		} catch(Exception ignored) {
		}
		return null;
	}

	public static File load(String path, String name) {
		File file = new File(ResourceLoader.class.getClassLoader().getResource(path).getPath(), name);
		return file;
	}
}
