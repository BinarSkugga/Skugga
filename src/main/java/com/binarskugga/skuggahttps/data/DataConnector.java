package com.binarskugga.skuggahttps.data;

public abstract class DataConnector<R> {

	protected abstract R create();

	public void initialize(DataInitializer initializer) {
		initializer.initialize();
	}

}
