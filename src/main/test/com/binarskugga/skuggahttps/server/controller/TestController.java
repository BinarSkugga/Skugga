package com.binarskugga.skuggahttps.server.controller;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.utils.*;

@Controller("test")
public class TestController extends AbstractController {

	@Get
	public Response ping() {
		return Response.ok();
	}

	@Get("image/jpg/{int}")
	public TransformableImage image_jpg(int size) {
		return new TransformableImage(ResourceLoader.load("", "image.jpg")).resize(size);
	}

	@Get("image/png/{int}")
	public TransformableImage image_png(int size) {
		return new TransformableImage(ResourceLoader.load("", "image.png")).resize(size);
	}

	@Get
	@ContentType("text/plain")
	public String text() {
		return "Hello :)";
	}

}
