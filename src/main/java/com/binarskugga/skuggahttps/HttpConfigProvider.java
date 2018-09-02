package com.binarskugga.skuggahttps;

import lombok.*;
import com.binarskugga.skuggahttps.utils.*;

public class HttpConfigProvider {

	private static PropertiesConfiguration config;

	private HttpConfigProvider() {
	}

	@Synchronized
	public static PropertiesConfiguration get() {
		if(config == null) {
			config = new PropertiesConfiguration("http.properties");
		}
		return config;
	}
}
