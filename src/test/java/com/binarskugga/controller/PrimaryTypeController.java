package com.binarskugga.controller;

import com.binarskugga.skugga.*;
import com.binarskugga.skugga.api.annotation.*;
import com.binarskugga.skugga.api.impl.endpoint.*;
import org.bson.types.*;

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
