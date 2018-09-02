package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.auth.*;
import com.sun.net.httpserver.*;
import lombok.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.utils.*;

import javax.net.ssl.*;
import java.net.*;
import java.security.*;
import java.util.concurrent.*;
import java.util.logging.*;

@NoArgsConstructor
public class HttpServer {

	@Getter private int port;
	@Getter private String host;

	private com.sun.net.httpserver.HttpServer server;
	private KeyStore keyStore;
	private SSLContext sslContext;

	private ExecutorService executor = null;
	private int executorSize;

	private AbstractHttpExchangeHandler exchangeHandler;

	private PropertiesConfiguration configuration;
	private Logger logger;

	public HttpServer(String host, int port, AbstractHttpExchangeHandler exchangeHandler) {
		this.port = port;
		this.host = host;
		this.exchangeHandler = exchangeHandler;

		this.configuration = HttpConfigProvider.get();
		this.logger = Logger.getLogger(HttpServer.class.getName());

		AuthService.get();
	}

	public void addSSLCertificate(String resourceName, String password) {
		try {
			this.sslContext = SSLContext.getInstance("TLS");

			char[] passArray = password.toCharArray();
			this.keyStore = KeyStore.getInstance("PKCS12");
			this.keyStore.load(ResourceLoader.load(resourceName), passArray);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(this.keyStore, passArray);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(this.keyStore);

			this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		} catch(Exception e) {
			this.logger.log(Level.SEVERE, "Couldn't create the SSL context, aborting !", e.getCause());
		}
	}

	public void setThreadPoolSize(int size) {
		this.executor = Executors.newFixedThreadPool(size);
		this.executorSize = size;
	}

	public void start() {
		try {
			if(this.sslContext != null) {
				HttpsServer server = HttpsServer.create(new InetSocketAddress(host, port), 1);
				server.setHttpsConfigurator(new HttpsConfigurator(this.sslContext) {
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
							HttpServer.this.logger.log(Level.SEVERE, "", e.getCause());
						}
					}
				});
				this.server = server;
			} else {
				this.server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(host, port), 1);
			}
		} catch(Exception e) {
			this.logger.log(Level.SEVERE, "", e.getCause());
		}

		this.server.createContext(this.configuration.getString("server.root"), this.exchangeHandler);

		this.server.setExecutor(this.executor);
		this.server.start();

		if(this.sslContext != null) {
			this.logger.info(String.format("Started HTTPS Server on https://%s:%d" + this.configuration.getString("server.root"), this.host, this.port));
		} else {
			this.logger.info(String.format("Started HTTP Server on http://%s:%d" + this.configuration.getString("server.root"), this.host, this.port));
		}

		if(this.executor != null) {
			this.logger.config(String.format("Using a thread pool of %d threads.", this.executorSize));
		}
	}

	public void stop() {
		if(this.executor != null) {
			this.executor.shutdown();
		}
		this.server.stop(0);
	}
}
