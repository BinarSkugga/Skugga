package com.binarskugga.skuggahttps.server.controller;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.auth.role.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;

@Controller("test")
public class TestController extends AbstractController {

	@Get
	@Access({ AllAccess.class })
	public Response ping() {
		return Response.ok();
	}

}
