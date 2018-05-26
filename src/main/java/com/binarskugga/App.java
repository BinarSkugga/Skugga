package com.binarskugga;

import com.binarskugga.skuggahttps.*;

public class App {

	public static void main(String[] args) {
		HttpsServer server = new HttpsServer("127.0.0.1", 5002)
				.ssl("keystore.p12", "ilovetacos")
				.controllers("com.binarskugga.app").threaded(4)
				.start();
	}

}
