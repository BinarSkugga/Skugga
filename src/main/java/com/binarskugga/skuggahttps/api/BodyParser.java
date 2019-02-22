package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.HttpSession;

import java.lang.reflect.Type;

public interface BodyParser {

	Object toObject(HttpSession session, String data, Type[] clazz, Class collectionClazz) throws Exception;
	String toString(HttpSession session, Object data, Type[] clazz, Class collectionClazz) throws Exception;

}
