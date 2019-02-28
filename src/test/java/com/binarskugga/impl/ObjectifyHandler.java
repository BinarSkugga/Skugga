package com.binarskugga.impl;

import com.binarskugga.skugga.api.RequestHandler;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
import com.googlecode.objectify.ObjectifyService;

import java.io.Closeable;

public class ObjectifyHandler implements RequestHandler {

	private Closeable closeable;

	@Override
	public boolean handle(HttpSession session) throws Exception {
		this.closeable = ObjectifyService.begin();
		return true;
	}

	@Override
	public void handlePostRequest(HttpSession session) throws Exception {
		this.closeable.close();
	}

}
