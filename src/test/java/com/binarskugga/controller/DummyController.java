package com.binarskugga.controller;

import com.binarskugga.skugga.api.annotation.Controller;
import com.binarskugga.skugga.api.annotation.Post;
import com.binarskugga.skugga.api.impl.endpoint.AbstractController;

import java.util.Collection;

@Controller("dummy")
public class DummyController extends AbstractController {

	@Post
	public void test(String t) { }

	@Post
	public Collection<String> testList(Collection<String> t) {
		return t;
	}

}
