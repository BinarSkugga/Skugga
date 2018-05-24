package com.binarskugga;

public class App {

	public static void main(String[] args) {
		new HttpsServer("127.0.0.1", 5002)
				.ssl("./src/main/resources/keystore.p12", "ilovetacos")
				.start();
	}

}
