package com.binarskugga.skuggahttps.server.controller;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.response.*;
import com.binarskugga.skuggahttps.server.dto.*;
import com.binarskugga.skuggahttps.server.exception.*;
import com.binarskugga.skuggahttps.utils.*;

@Controller("test")
public class TestController extends AbstractController {

	@Get
	public Response ping() {
		return Response.ok();
	}

	@Get("image/jpg/{int}")
	public HttpImage image_jpg(int size) {
		return new HttpImage(ResourceLoader.load("", "image.jpg")).resize(size);
	}

	@Get("image/png/{int}")
	public HttpImage image_png(int size) {
		return new HttpImage(ResourceLoader.load("", "image.png")).resize(size);
	}

	@Get
	public HttpString text() {
		return new HttpString("Hello :)");
	}

	@Get
	public Response apierror() {
		throw new TestAPIException();
	}

	@Get
	public Response httperror() {
		throw new TestHttpException();
	}

	@Post
	public TestDTO validateme(TestDTO dto) {
		return dto;
	}

}
