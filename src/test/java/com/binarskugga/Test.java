package com.binarskugga;

import com.binarskugga.impl.LocalObjectifyConnector;
import com.binarskugga.impl.ObjectifyHandler;
import com.binarskugga.skugga.Skugga;
import com.binarskugga.skugga.api.impl.handler.DefaultLoggingHandler;
import com.binarskugga.skugga.util.ResourceUtils;
import com.binarskugga.skugga.util.conversion.PrimitiveConversionUtils;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;

import java.util.logging.LogManager;

public class Test {

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(ResourceUtils.getLoggingProperties());

		Boolean[] data = new Boolean[] { true, false, false, false };
		byte[] l = PrimitiveConversionUtils.array(Boolean[].class).convertTo(byte[].class, data);

		LocalObjectifyConnector connector = new LocalObjectifyConnector(LocalDatastoreHelper.create());
		Skugga server = new Skugga(connector, new ObjectifyHandler(), new DefaultLoggingHandler());
		server.start();
	}

}
