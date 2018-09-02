package com.binarskugga.skuggahttps.utils;

import lombok.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class PropertiesConfiguration {

	private static Logger logger = Logger.getLogger(PropertiesConfiguration.class.getName());

	@Getter private Properties properties;

	public PropertiesConfiguration(String resourceName) {
		try {
			this.properties = new Properties();
			this.properties.load(PropertiesConfiguration.class.getClassLoader().getResourceAsStream(resourceName));
		} catch(IOException e) {
			logger.log(Level.SEVERE, "", e.getCause());
		}
	}

	public Optional<String> getString(String key) {
		return Optional.ofNullable(this.properties.getProperty(key));
	}

	public Optional<Integer> getInt(String key) {
		return Optional.ofNullable(PropertiesUtils.parseInt(this.properties.getProperty(key)));
	}

	public Optional<Float> getFloat(String key) {
		return Optional.ofNullable(PropertiesUtils.parseFloat(this.properties.getProperty(key)));
	}

	public Optional<Double> getDouble(String key) {
		return Optional.ofNullable(PropertiesUtils.parseDouble(this.properties.getProperty(key)));
	}

	public Optional<Boolean> getBoolean(String key) {
		return Optional.ofNullable(PropertiesUtils.parseBoolean(this.properties.getProperty(key)));
	}

}
