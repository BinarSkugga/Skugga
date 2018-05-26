package com.binarskugga;

import com.binarskugga.skuggahttps.*;

public class App {

	public static void main(String[] args) {
		new HttpsServer("127.0.0.1", 5002)
				.ssl("keystore.p12", "ilovetacos")
				.threaded(4).start();
	}

}
