package com.binarskugga.skugga.api.impl.handler;

import com.binarskugga.skugga.api.RequestHandler;
import com.binarskugga.skugga.api.exception.http.HttpException;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
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
		logger.atInfo().log(session.getRequestMethod() + " " + session.getExchange().getRequestPath() + ": Processed in " + (System.currentTimeMillis() - time) + "ms");
	}

	@Override
	public void handleException(HttpSession session, Exception e) {
		if (HttpException.class.isAssignableFrom(e.getClass())) {
			logger.atWarning().log(session.getRequestMethod() + " " + session.getExchange().getRequestPath() + ": " + e.getMessage());
		} else {
			if (e.getMessage() != null)
				logger.atSevere().withCause(e).log(session.getRequestMethod() + " " + session.getExchange().getRequestPath() + ": " + e.getMessage());
			else
				logger.atSevere().withCause(e).log();
		}
	}

}
