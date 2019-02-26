package com.binarskugga.skuggahttps.api.impl.handler;

import com.binarskugga.skuggahttps.api.RequestHandler;
import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.google.common.flogger.FluentLogger;

public class DefaultLoggingHandler implements RequestHandler {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	private long time;

	@Override
	public boolean handle(HttpSession session) {
		this.time = System.currentTimeMillis();
		return true;
	}

	@Override
	public void handlePostRequest(HttpSession session) {
		logger.atInfo().log(session.getExchange().getRequestMethod() + " " + session.getExchange().getRequestPath() + ": Processed in " + (System.currentTimeMillis() - time) + "ms");
	}

	@Override
	public void handleException(HttpSession session, Exception e) {
		if(e.getMessage() != null)
			logger.atInfo().log(session.getExchange().getRequestMethod() + " " + session.getExchange().getRequestPath() + ":" + e.getMessage());
		else
			logger.atSevere().withCause(e).log();
	}

}
