package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.exception.*;
import com.google.common.base.*;
import com.sun.net.httpserver.*;
import org.slf4j.*;

import java.io.*;
import java.util.zip.*;

public abstract class AbstractHttpExchangeHandler implements HttpHandler {

	private PropertiesConfiguration configuration;
	private Logger logger;

	public AbstractHttpExchangeHandler() {
		this.configuration = new PropertiesConfiguration("http.properties");
		this.logger = LoggerFactory.getLogger(HttpsServer.class);
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		OutputStream os = exchange.getResponseBody();

		Headers inHeaders = exchange.getRequestHeaders();
		Headers outHeaders = exchange.getResponseHeaders();
		boolean acceptGzip = inHeaders.get("Accept-Encoding").get(0).contains("gzip");

		this.logger.info(exchange.getRequestMethod() + " Request at " + exchange.getRequestURI());

		if(acceptGzip)
			outHeaders.add("Content-Encoding", this.configuration.getString("headers.content-encoding"));

		outHeaders.add("Connection", this.configuration.getString("headers.connection"));
		outHeaders.add("Strict-Transport-Security", this.configuration.getString("headers.strict-transport-security"));
		outHeaders.add("Transfer-Encoding", this.configuration.getString("headers.transfer-encoding"));
		outHeaders.add("Content-Type", this.configuration.getString("headers.content-type"));
		outHeaders.add("Server", this.configuration.getString("headers.server"));
		outHeaders.add("Cache-Control", this.configuration.getString("headers.cache-control"));
		outHeaders.add("Vary", this.configuration.getString("headers.vary"));

		outHeaders.add("Access-Control-Allow-Origin", this.configuration.getString("headers.cors.access-control-allow-origin"));
		outHeaders.add("Access-Control-Allow-Methods", this.configuration.getString("headers.cors.access-control-allow-methods"));
		outHeaders.add("Access-Control-Max-Age", this.configuration.getString("headers.cors.access-control-max-age"));

		Response response = Response.notfound();
		try {
			if(exchange.getRequestMethod().equals("GET"))
				response = onGet(outHeaders, inHeaders, exchange.getRequestURI().getPath());
			else if(exchange.getRequestMethod().equals("POST"))
				response = onPost(outHeaders, inHeaders, exchange.getRequestURI().getPath(), exchange.getRequestBody());
		} catch(HTTPException e) {
			this.logger.error("HTTP ERROR " + e.getStatus(), e);
			response = Response.create(e.getStatus(), e.getMessage());
		}

		exchange.sendResponseHeaders(response.status, 0);

		if(acceptGzip)
			os = new GZIPOutputStream(os);
		if(response.body != null)
			os.write(response.body.getBytes(Charsets.UTF_8));

		os.close();
		exchange.close();
	}

	public abstract Response onGet(Headers outHeaders, Headers inHeaders, String path);
	public abstract Response onPost(Headers outHeaders, Headers inHeaders, String path, InputStream body);

}
