package com.binarskugga.app;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.annotation.*;

@Controller("main")
public class MainController {

	@Get("hello")
	public Response hello() {
		return Response.ok("{\"value\":\"hello\"}");
	}

	@Post("bonjour")
	public Response bonjour() {
		return Response.ok();
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
