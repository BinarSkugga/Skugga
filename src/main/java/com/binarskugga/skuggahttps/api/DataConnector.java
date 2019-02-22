package com.binarskugga.skuggahttps.api;

public interface DataConnector<T> {

	T connect(String modelPackage);

}
