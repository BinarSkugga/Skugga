package com.binarskugga.controller;

import com.binarskugga.skugga.api.annotation.Controller;
import com.binarskugga.skugga.api.annotation.Get;
import com.binarskugga.skugga.api.impl.endpoint.AbstractController;
import org.bson.types.ObjectId;

@Controller("primary-type")
public class PrimaryTypeController extends AbstractController {

	@Get("test-string/$")
	public String test_get_string(String s) {
		return s;
	}

	@Get("test-astring/$")
	public String[] test_get_astring(String[] s) {
		return s;
	}

	@Get("test-oid/$")
	public ObjectId test_get_astring(ObjectId o) {
		return o;
	}

}
