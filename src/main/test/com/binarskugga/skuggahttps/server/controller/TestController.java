package com.binarskugga.skuggahttps.server.controller;

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
	@ContentType("image/jpg")
	public byte[] image_jpg() {
		try {
			BufferedImage bimg = ImageIO.read(ResourceLoader.load("image.jpg"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "jpg", bos);
			return bos.toByteArray();
		} catch(Exception e) {
			return new byte[]{};
		}
	}

	@Get
	@ContentType("image/png")
	public byte[] image_png() {
		try {
			BufferedImage bimg = ImageIO.read(ResourceLoader.load("image.png"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "png", bos);
			return bos.toByteArray();
		} catch(Exception e) {
			return new byte[]{};
		}
	}

	@Get
	@ContentType("text/plain")
	public String text() {
		return "Hello :)";
	}

}
