package com.binarskugga;

import com.sun.net.httpserver.*;

import java.io.*;
import java.nio.charset.*;
import java.util.zip.*;

public abstract class AbstractHttpExchangeHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		OutputStream os = exchange.getResponseBody();

		String data = "{\"data\":\"Hello\", \"level\":20}";

		Headers inHeaders = exchange.getRequestHeaders();
		Headers outHeaders = exchange.getResponseHeaders();
		boolean acceptGzip = inHeaders.get("Accept-Encoding").get(0).contains("gzip");

		// TODO: All headers here should be modifiable through a config file
		outHeaders.add("Transfer-Encoding", "chunked");
		outHeaders.add("Content-Type", "text/json; charset=UTF-8");
		outHeaders.add("Access-Control-Allow-Origin", "*");
		outHeaders.add("Access-Control-Allow-Methods", "POST, GET");
		outHeaders.add("Access-Control-Max-Age", "3600");
		outHeaders.add("Server", "Java 1.8-HTTPS");

		if(acceptGzip) {
			outHeaders.add("Content-Encoding", "gzip");
			outHeaders.add("Vary", "Accept-Encoding");
		}

		exchange.sendResponseHeaders(200, 0);

		if(acceptGzip)
			os = new GZIPOutputStream(os);

		os.write(data.getBytes(StandardCharsets.UTF_8));
		os.flush();
		os.close();

		exchange.close();
	}

	public abstract void onGet();
	public abstract void onPut();

}
