package com.binarskugga.skuggahttps.server;

import com.binarskugga.skuggahttps.http.*;
import com.squareup.moshi.*;

import java.io.*;
import java.util.logging.*;

public class MoshiJsonHandler implements HttpJsonHandler {

	private static Moshi moshi = MoshiProvider.get();
	private static Logger logger = Logger.getLogger(MoshiJsonHandler.class.getName());

	@Override
	public <T> String toJson(Class<T> clazz, T response) {
		JsonAdapter<T> adapter = moshi.adapter(clazz);
		return adapter.toJson(response);
	}

	@Override
	public <T> T fromJson(Class<T> clazz, String body) {
		JsonAdapter<T> adapter = moshi.adapter(clazz);
		try {
			return adapter.fromJson(body);
		} catch (IOException e) {
			logger.severe("Couldn't create object from JSON. (" + clazz.getName() + ")");
			e.printStackTrace();
		}
		return null;
	}
}
