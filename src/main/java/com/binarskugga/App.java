package com.binarskugga;

public class App {

	public static void main(String[] args) {
		new SkuggaServer("127.0.0.1", 5002)
				.ssl("./src/main/resources/keystore.jks", "ilovetacos")
				.start();
	}

}
