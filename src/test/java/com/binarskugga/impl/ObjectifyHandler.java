package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.impl.*;
import com.googlecode.objectify.*;

import java.io.*;
import java.util.concurrent.*;

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
