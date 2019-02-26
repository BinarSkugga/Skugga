package com.binarskugga;

import com.binarskugga.impl.*;
import com.binarskugga.skuggahttps.SkuggaHttp;
import com.binarskugga.skuggahttps.SkuggaHttpHandler;
import com.binarskugga.skuggahttps.api.impl.ServerProperties;
import com.binarskugga.skuggahttps.util.*;
import com.google.auth.oauth2.*;

import java.util.logging.LogManager;

public class Test {

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(ResourceUtils.getLoggingProperties());
		ServerProperties.load(ResourceUtils.getServerProperties());

		ObjectifyConnector connector = new ObjectifyConnector("sru-api",
				GoogleCredentials.fromStream(ResourceUtils.getCompiledResource("google-account.json")));
		connector.connect(ServerProperties.getModelPackage());

		SkuggaHttpHandler handler = new SkuggaHttpHandler();
		handler.append(new ObjectifyHandler());

		SkuggaHttp server = new SkuggaHttp(handler);
		server.start();
	}

}
