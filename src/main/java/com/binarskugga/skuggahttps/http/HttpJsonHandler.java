package com.binarskugga.skuggahttps.http;

public interface HttpJsonHandler {

	<T> String toJson(Class<T> clazz, T response);
	<T> T fromJson(Class<T> clazz, String body);

}
