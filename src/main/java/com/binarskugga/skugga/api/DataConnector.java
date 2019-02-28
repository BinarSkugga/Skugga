package com.binarskugga.skugga.api;

public interface DataConnector<T> {

	T connect(String modelPackage);

}
