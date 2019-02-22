package com.binarskugga;

import com.binarskugga.skuggahttps.SkuggaHttp;
import com.binarskugga.skuggahttps.SkuggaHttpHandler;
import com.binarskugga.skuggahttps.util.ResourceUtils;

import java.util.logging.LogManager;

public class Test {

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(ResourceUtils.getCompiledResource("logging.properties"));
		SkuggaHttp server = new SkuggaHttp(new SkuggaHttpHandler());
		server.start();
	}

}
