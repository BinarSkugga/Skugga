package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.endpoint.HttpSession;

public interface RequestHandler {

	boolean handle(HttpSession session) throws Exception;
	default void handlePostRequest(HttpSession session) throws Exception {}
	default void handleException(HttpSession session, Exception e) {}

}
