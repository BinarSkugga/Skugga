package com.binarskugga.controller;

import com.binarskugga.model.TestModel;
import com.binarskugga.skuggahttps.api.annotation.*;
import com.binarskugga.skuggahttps.api.impl.endpoint.AbstractController;
import com.binarskugga.skuggahttps.api.impl.parse.MapParser;
import com.binarskugga.skuggahttps.util.EntityUtils;

import java.util.Map;

@Controller("dummy")
public class DummyController extends AbstractController {

	@Post
	public Map<String, Object> bonjour(Map<String, Object> map) {
		TestModel model = (TestModel) MapParser.parse(EntityUtils.getCreateFields(TestModel.class, null), map);
		return MapParser.unparse(EntityUtils.getCreateFields(TestModel.class, null), model);
	}

	@Get("params/$/$/$")
	public String params(Class test, int test1, long[] test2) {
		return "params";
	}

}
