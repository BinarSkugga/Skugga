package com.binarskugga.skuggahttps.server;

import com.squareup.moshi.*;
import org.bson.types.*;

public class ObjectIdMapper {

	@FromJson public ObjectId fromJson(String json) {
		return new ObjectId(json);
	}

	@ToJson public String toJson(ObjectId object) {
		return object.toHexString();
	}

}
