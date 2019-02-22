package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.api.HttpServer;
import com.binarskugga.skuggahttps.api.impl.handler.AccessControlHandler;
import com.binarskugga.skuggahttps.api.impl.handler.AuthHandler;
import com.binarskugga.skuggahttps.api.impl.handler.DefaultLoggingHandler;
import com.google.common.flogger.FluentLogger;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.*;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SkuggaHttp implements HttpServer {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private Undertow.Builder utBuilder;
	private Undertow ut;

	private SkuggaHttpHandler handler;

	public SkuggaHttp(SkuggaHttpHandler handler) {
		this.handler = handler;
		utBuilder = Undertow.builder();
	}

	@Override
	public void start() {
		if(this.handler.getHost().equalsIgnoreCase("0.0.0.0")) {
			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				logger.atInfo().log("Server is set on the '0.0.0.0' host which means it can be accessed from a LAN ip.");
				logger.atInfo().log("Skugga starting on http://127.0.0.1:" + this.handler.getPort() + "/" + this.handler.getRoot()
						+ " or http://" + inetAddress.getHostAddress() + ":" + this.handler.getPort() + "/" + this.handler.getRoot() + "...");
			} catch (UnknownHostException ignored) {}
		} else {
			logger.atInfo().log("Skugga starting on http://" + this.handler.getHost() + ":" + this.handler.getPort() + "...");
		}
		this.handler.prepend(new AuthHandler());
		this.handler.prepend(new AccessControlHandler());
		this.handler.append(new DefaultLoggingHandler());

		PathHandler mainHandler = Handlers.path();
		mainHandler.addPrefixPath(this.handler.getServerProperties().getRoot(), new EncodingHandler(new ContentEncodingRepository()
				.addEncodingHandler("gzip", new GzipEncodingProvider(), 50, Predicates.maxContentSize(100)))
				.setNext(this.handler));

		this.ut = this.utBuilder
			.addHttpListener(this.handler.getPort(), this.handler.getHost())
			.setHandler(mainHandler).build();
		this.ut.start();
		logger.atInfo().log("Skugga can now receive requests !");
	}

	@Override
	public int stop() {
		if(this.ut == null) {
			logger.atWarning().log("Skugga can't close because it never started.");
			return -1;
		}

		logger.atInfo().log("Skugga is closing ! Bye bye :)");
		this.ut.stop();
		return 0;
	}

}
