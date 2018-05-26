package com.binarskugga.skuggahttps;

import com.google.common.io.*;

import java.io.*;
import java.util.*;

public class PropertiesConfiguration {

	private Properties properties;

	public PropertiesConfiguration(String resourceName) {
		try {
			this.properties = new Properties();
			this.properties.load(new FileInputStream(Resources.getResource(resourceName).getPath()));
		} catch(IOException e) {
			// TODO: Define this
			e.printStackTrace();
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
