package com.binarskugga.skuggahttps;

import com.google.common.io.*;
import com.sun.net.httpserver.*;
import org.slf4j.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.concurrent.*;

public class HttpsServer {

	private int port;
	private String host;

	private KeyStore keyStore;
	private com.sun.net.httpserver.HttpsServer server;
	private SSLContext sslContext;

	private ExecutorService executor = null;
	private int executorSize;

	private Logger logger = LoggerFactory.getLogger(HttpsServer.class);

	public HttpsServer(String host, int port) {
		this.port = port;
		this.host = host;

		try {
			this.server = com.sun.net.httpserver.HttpsServer.create(new InetSocketAddress(host, port), 1);
			this.sslContext = SSLContext.getInstance("TLS");
		} catch(Exception e) {
			// TODO: Define this
			e.printStackTrace();
		}
	}

	public HttpsServer ssl(String resourceName, String password) {
		try {
			char[] passArray = password.toCharArray();
			this.keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream stream = new FileInputStream(Resources.getResource(resourceName).getPath());
			this.keyStore.load(stream, passArray);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(this.keyStore, passArray);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(this.keyStore);

			this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			this.server.setHttpsConfigurator(new HttpsConfigurator(this.sslContext) {
				@Override
				public void configure(HttpsParameters params) {
					try {
						SSLContext context = SSLContext.getDefault();
						SSLEngine engine = context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());

						SSLParameters sslParams = context.getDefaultSSLParameters();
						params.setSSLParameters(sslParams);
					} catch(NoSuchAlgorithmException e) {
						// TODO: Define this
						e.printStackTrace();
					}
				}
			});
		} catch(Exception e) {
			// TODO: Define this
			e.printStackTrace();
		}
		return this;
	}

	public HttpsServer threaded(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
		this.executorSize = threads;
		return this;
	}

	public HttpsServer start() {
		this.server.createContext("/", new AbstractHttpExchangeHandler() {
			@Override
			public Response onGet(Headers outHeaders, Headers inHeaders, String path) {
				return Response.ok();
			}

			@Override
			public Response onPost(Headers outHeaders, Headers inHeaders, String path) {
				return Response.ok();
			}
		});
		this.server.setExecutor(this.executor);
		this.server.start();
		this.logger.info("Started HTTPS Server on " + this.host + ":" + this.port);
		if(this.executor != null)
			this.logger.debug("Using a thread pool of " + this.executorSize + " threads.");
		return this;
	}

}
