package com.binarskugga.impl;

import com.binarskugga.skugga.api.BodyParser;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.parse.BodyInformation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;

public class MoshiBodyParser implements BodyParser<String> {

	@Override
	public Object parse(BodyInformation information, String body) {
		try {
			return this.createAdapter(information.getInnerTypes(), information.getCollectionClass()).fromJson(body);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String unparse(BodyInformation information, Object body) {
		try {
			return this.createAdapter(information.getInnerTypes(), information.getCollectionClass()).toJson(body);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private JsonAdapter createAdapter(Type[] clazz, Class collectionClazz) {
		Moshi moshi = MoshiProvider.get();
		if (collectionClazz == null)
			return moshi.adapter(clazz[0]);
		else {
			return moshi.adapter(Types.newParameterizedType(collectionClazz, clazz));
		}
	}

	@Override
	public boolean predicate(Endpoint c) {
		String contentType = c.getContentType();
		return contentType.equalsIgnoreCase("application/json")
				|| contentType.equalsIgnoreCase("text/json");
	}

}
