package com.binarskugga.app;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.annotation.*;

@Controller("main")
public class MainController {

	@Get("hello/{int}")
	public Response hello(int blop) {
		return Response.ok("{\"value\":" + blop + "}");
	}

	@Post("bonjour")
	public Response bonjour(String body) {
		return Response.ok(body);
	}

	@Get("salut")
	public Response salut() {
		return Response.ok("{\"value\":\"salut\"}");
	}

	@Post("boi")
	public Response boi() {
		return Response.ok();
	}

}
