package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.annotation.ContentType;
import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@ContentType("application/json")
public class MoshiParser implements BodyParser {

	@Override
	public Object toObject(HttpSession session, String body, Type[] clazz, Class collectionClazz) throws Exception {
		return this.createAdapter(clazz, collectionClazz).fromJson(body);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString(HttpSession session, Object body, Type[] clazz, Class collectionClazz) {
		return this.createAdapter(clazz, collectionClazz).toJson(body);
	}

	@SuppressWarnings("unchecked")
	private JsonAdapter createAdapter(Type[] clazz, Class collectionClazz) {
		Moshi moshi = MoshiProvider.get();
		if(collectionClazz == null)
			return moshi.adapter(clazz[0]);
		else {
			return moshi.adapter(Types.newParameterizedType(collectionClazz, clazz));
		}
	}

}
