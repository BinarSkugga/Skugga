package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.ExceptionParser;
import com.binarskugga.skuggahttps.api.annotation.ContentType;
import com.binarskugga.skuggahttps.api.exception.http.HttpException;
import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.HashMap;
import java.util.Map;

@ContentType("application/json")
public class MoshiExceptionParser implements ExceptionParser {

	@Override
	public String toString(HttpSession session, Throwable throwable) {
		Moshi moshi = MoshiProvider.get();
		JsonAdapter<Map<String, Object>> adapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
		Map<String, Object> data = new HashMap<>();
		data.put("code", session.getExchange().getStatusCode());
		data.put("type", throwable.getClass().getName());

		if(HttpException.class.isAssignableFrom(throwable.getClass())) {
			HttpException exception = (HttpException) throwable;
			data.put("caption", exception.getMessage());
		}

		return adapter.toJson(data);
	}

}
