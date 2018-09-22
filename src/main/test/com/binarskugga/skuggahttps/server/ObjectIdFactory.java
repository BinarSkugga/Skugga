package com.binarskugga.skuggahttps.server;

import org.bson.types.*;

public class ObjectIdFactory {

	private ObjectIdFactory() {}

	public static ObjectId create(String hex) {
		try {
			return new ObjectId(hex);
		} catch(Exception e) {
			return null;
		}
	}

}
