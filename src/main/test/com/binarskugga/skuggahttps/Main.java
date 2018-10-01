package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.server.*;

import java.io.*;
import java.util.logging.*;

public class Main {

	public static void main(String[] args) throws IOException {
		LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
		HttpServer server = new HttpServer(new TestExchangeHandler());
		server.start();
	}

}
