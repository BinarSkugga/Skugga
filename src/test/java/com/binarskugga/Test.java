package com.binarskugga;

import com.binarskugga.impl.LocalObjectifyConnector;
import com.binarskugga.impl.ObjectifyHandler;
import com.binarskugga.skugga.Skugga;
import com.binarskugga.skugga.util.ResourceUtils;

import java.util.logging.LogManager;

public class Test {

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(ResourceUtils.getLoggingProperties());
		Skugga server = new Skugga(new LocalObjectifyConnector(), new ObjectifyHandler());
		server.start();
	}

}
