package com.binarskugga;

import com.binarskugga.impl.*;
import com.binarskugga.skuggahttps.SkuggaHttp;
import com.binarskugga.skuggahttps.SkuggaHttpHandler;
import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.util.*;
import com.google.auth.oauth2.*;

import java.util.logging.LogManager;

public class Test {

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(ClassLoader.getSystemClassLoader().getResourceAsStream("logging.properties"));

		ObjectifyConnector connector = new ObjectifyConnector("tutorfinder-222822",
				GoogleCredentials.fromStream(ResourceUtils.getCompiledResource("google-account.json")));
		connector.connect("com.binarskugga.model");

		SkuggaHttpHandler handler = new SkuggaHttpHandler();
		handler.append(new ObjectifyHandler());

		SkuggaHttp server = new SkuggaHttp(handler);
		server.start();
	}

}
