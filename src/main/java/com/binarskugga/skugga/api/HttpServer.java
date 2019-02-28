package com.binarskugga.skugga.api;

public interface HttpServer {

	void start();

	int stop();

	default int restart() {
		int stopCode = this.stop();
		start();
		return stopCode;
	}

}