package com.binarskugga.skuggahttps.server;

import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.data.*;
import com.binarskugga.skuggahttps.http.*;
import org.bson.types.*;

public class TestExchangeHandler extends AbstractHttpExchangeHandler<ObjectId> {

	@Override
	public HttpJsonHandler getJsonHandler() {
		return new MoshiJsonHandler();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Q, T extends Identifiable> DataRepository<Q, ObjectId, T> getIdentityRepository() {
		return null;
	}

	@Override
	public ObjectId createID(String s) {
		return ObjectIdFactory.create(s);
	}

}
