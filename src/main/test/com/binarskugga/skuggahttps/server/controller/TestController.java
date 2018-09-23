package com.binarskugga.skuggahttps.server.controller;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.utils.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

@Controller("test")
public class TestController extends AbstractController {

	@Get
	public Response ping() {
		return Response.ok();
	}

	@Get
	public TransformableImage image_jpg() {
		return new TransformableImage(ResourceLoader.load("", "image.jpg"));
	}

	@Get
	public TransformableImage image_png() {
		return new TransformableImage(ResourceLoader.load("", "image.png"));
	}

	@Get
	@ContentType("text/plain")
	public String text() {
		return "Hello :)";
	}

}
