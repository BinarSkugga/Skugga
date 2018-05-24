package com.binarskugga;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;

public class SkuggaServer {

	private int port;
	private String host;

	private KeyStore keyStore;
	private HttpsServer server;
	private SSLContext sslContext;

	public SkuggaServer(String host, int port) {
		this.port = port;
		this.host = host;

		try {
			this.server = HttpsServer.create(new InetSocketAddress(host, port), 1);
			this.sslContext = SSLContext.getInstance("TLS");
		} catch(Exception e) {
			// TODO: Define this
			e.printStackTrace();
		}
	}

	public SkuggaServer ssl(String path, String password) {
		try {
			char[] passArray = password.toCharArray();
			this.keyStore = KeyStore.getInstance("JKS");
			FileInputStream stream = new FileInputStream(path);
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

	public SkuggaServer start() {
		this.server.createContext("/", new SkuggaHandler());
		this.server.setExecutor(null);
		this.server.start();
		return this;
	}

}
