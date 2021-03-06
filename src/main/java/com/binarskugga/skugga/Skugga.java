package com.binarskugga.skugga;

import com.binarskugga.skugga.api.*;
import com.binarskugga.skugga.api.impl.handler.AccessControlHandler;
import com.binarskugga.skugga.api.impl.handler.AuthHandler;
import com.binarskugga.skugga.api.impl.handler.DefaultLoggingHandler;
import com.google.common.flogger.FluentLogger;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Skugga implements HttpServer {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private Undertow.Builder utBuilder;
	private Undertow ut;

	private SkuggaHandler handler;

	public Skugga(SkuggaHandler handler) {
		this.handler = handler;
		utBuilder = Undertow.builder();
	}

	public Skugga(DataConnector connector, RequestHandler... handlers) {
		this.handler = new SkuggaHandler(connector);
		for(RequestHandler handler : handlers)
			this.handler.append(handler);

		utBuilder = Undertow.builder();
	}

	@Override
	public void start() {
		if (ServerProperties.getIp().equalsIgnoreCase("0.0.0.0")) {
			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				logger.atInfo().log("Server is set on the '0.0.0.0' host which means it can be accessed from a LAN ip.");
				logger.atInfo().log("Skugga starting on http://127.0.0.1:" + ServerProperties.getPort() + "/" + ServerProperties.getRoot()
						+ " or http://" + inetAddress.getHostAddress() + ":" + ServerProperties.getPort() + "/" + ServerProperties.getRoot() + "...");
			} catch (UnknownHostException ignored) {
			}
		} else {
			logger.atInfo().log("Skugga starting on http://" + ServerProperties.getIp() + ":" + ServerProperties.getPort() + "...");
		}
		this.handler.prepend(new AuthHandler());
		this.handler.prepend(new AccessControlHandler());

		PathHandler mainHandler = Handlers.path();
		mainHandler.addPrefixPath(ServerProperties.getRoot(), new EncodingHandler(new ContentEncodingRepository()
				.addEncodingHandler("gzip", new GzipEncodingProvider(), 50, Predicates.maxContentSize(100)))
				.setNext(this.handler));

		this.ut = this.utBuilder
				.addHttpListener(ServerProperties.getPort(), ServerProperties.getIp())
				.setHandler(mainHandler).build();
		this.ut.start();
		logger.atInfo().log("Skugga can now receive requests !");
	}

	@Override
	public int stop() {
		if (this.ut == null) {
			logger.atWarning().log("Skugga can't close because it never started.");
			return -1;
		}

		logger.atInfo().log("Skugga is closing ! Bye bye :)");
		this.ut.stop();
		return 0;
	}

}
