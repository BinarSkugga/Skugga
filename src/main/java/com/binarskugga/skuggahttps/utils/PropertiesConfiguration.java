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
			this.properties.load(ResourceLoader.load(resourceName));
		} catch(IOException e) {
			logger.log(Level.SEVERE, "", e.getCause());
		}
	}

	public String getString(String key) {
		return this.properties.getProperty(key);
	}

	public int getInt(String key) {
		return Integer.parseInt(this.properties.getProperty(key));
	}

	public float getFloat(String key) {
		return Float.parseFloat(this.properties.getProperty(key));
	}

	public double getDouble(String key) {
		return Double.parseDouble(this.properties.getProperty(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(this.properties.getProperty(key));
	}
}
