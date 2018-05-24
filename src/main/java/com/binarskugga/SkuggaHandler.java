package com.binarskugga;

import com.sun.net.httpserver.*;

import java.io.*;

public class SkuggaHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		OutputStream os = exchange.getResponseBody();

		exchange.sendResponseHeaders(200, 0);
		os.write("Hello".getBytes());
		os.flush();
		os.close();
		exchange.close();
	}

}
