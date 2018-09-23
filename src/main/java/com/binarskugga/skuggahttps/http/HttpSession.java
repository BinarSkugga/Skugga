package com.binarskugga.skuggahttps.http;

import com.sun.net.httpserver.*;
import lombok.*;
import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.http.path.*;
import com.binarskugga.skuggahttps.utils.*;

import java.lang.reflect.*;
import java.util.*;

public class HttpSession {

	@Getter @Setter PropertiesConfiguration config;

	@Getter @Setter Endpoint endpoint;
	@Singular @Getter @Setter Map<Parameter, Object> args;

	@Getter @Setter private GenericUser identity;
	@Getter @Setter private HttpExchange exchange;

	@Getter @Setter Object body;
	@Getter @Setter Object responseBody;
	@Getter @Setter Response response;

	@Getter @Setter Map<String, Cookie> cookies;
	@Getter private Map<String, Object> extras = new HashMap<>();

	public void setExtra(String key, Object value) {
		this.extras.put(key, value);
	}

	public Object getExtra(String key) {
		return this.extras.get(key);
	}

}
