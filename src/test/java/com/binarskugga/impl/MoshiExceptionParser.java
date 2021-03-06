package com.binarskugga.impl;

import com.binarskugga.skugga.api.ExceptionParser;
import com.binarskugga.skugga.api.exception.http.HttpException;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
import com.google.common.base.CaseFormat;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.HashMap;
import java.util.Map;

public class MoshiExceptionParser implements ExceptionParser {

	@Override
	public Throwable parse(HttpSession context, Object object) {
		return null;
	}

	@Override
	public Object unparse(HttpSession session, Throwable throwable) {
		Moshi moshi = MoshiProvider.get();
		JsonAdapter<Map<String, Object>> adapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
		Map<String, Object> data = new HashMap<>();
		data.put("code", session.getExchange().getStatusCode());
		data.put("type", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, throwable.getClass().getSimpleName()));

		if (HttpException.class.isAssignableFrom(throwable.getClass())) {
			HttpException exception = (HttpException) throwable;
			data.put("caption", exception.getMessage());
		}

		return adapter.toJson(data);
	}

	@Override
	public boolean predicate(Endpoint c) {
		String contentType = c.getContentType();
		return contentType.equalsIgnoreCase("application/json")
				|| contentType.equalsIgnoreCase("text/json");
	}
}
