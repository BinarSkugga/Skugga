package com.binarskugga.controller;

import com.binarskugga.TestModel;
import com.binarskugga.skuggahttps.api.annotation.*;
import com.binarskugga.skuggahttps.api.impl.endpoint.AbstractController;
import com.binarskugga.skuggahttps.api.impl.map.DefaultEndpointMapper;
import com.binarskugga.skuggahttps.util.EntityUtils;

import java.util.Map;

@Controller("dummy")
public class DummyController extends AbstractController {

	private static DefaultEndpointMapper mapper = new DefaultEndpointMapper();

	@Post
	public Map<String, Object> bonjour(Map<String, Object> map) {
		TestModel model = (TestModel) mapper.toEntity(EntityUtils.getCreateFields(TestModel.class, null), map);
		return mapper.toMap(EntityUtils.getCreateFields(TestModel.class, null), model);
	}

	@Get("params/$/$/$")
	public String params(Class test, int test1, long[] test2) {
		return "params";
	}

}
