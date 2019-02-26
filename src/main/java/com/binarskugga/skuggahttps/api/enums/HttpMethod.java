package com.binarskugga.skuggahttps.api.enums;

import com.google.common.base.Joiner;

import java.util.List;
import java.util.stream.Collectors;

public enum HttpMethod {

	GET, HEAD, POST(true), PUT(true), DELETE, OPTIONS, CONNECT;

	private boolean acceptBody;

	HttpMethod() {
		this.acceptBody = false;
	}

	HttpMethod(boolean acceptBody) {
		this.acceptBody = acceptBody;
	}

	public static HttpMethod fromMethodString(String str) {
		for (HttpMethod m : HttpMethod.values())
			if (m.name().equalsIgnoreCase(str)) return m;
		return null;
	}

	public static String toHeaderListString(List<HttpMethod> headers) {
		return Joiner.on(",").join(headers.stream().map(HttpMethod::name).collect(Collectors.toList()));
	}

	public boolean acceptBody() {
		return acceptBody;
	}

}
