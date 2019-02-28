package com.binarskugga.skugga.api.enums;

import com.google.common.base.Joiner;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.binarskugga.skugga.api.enums.HeaderType.*;

public enum HttpHeader {

	WWW_AUTHENTICATE("WWW-Authenticate", RESPONSE), AUTHORIZATION("Authorization", REQUEST),
	PROXY_AUTHENTICATE("Proxy-Authenticate", RESPONSE), PROXY_AUTHORIZATION("Proxy-Authorization", REQUEST),

	AGE("Age", RESPONSE), CACHE_CONTROL("Cache-Control"), CLEAR_SITE_DATA("Clear-Site-Data", RESPONSE),
	EXPIRES("Expires", RESPONSE), PRAGMA("Pragma"), WARNING("Warning"),

	LAST_MODIFIED("Last-Modified", RESPONSE), ETAG("ETag", RESPONSE), VARY("Vary", RESPONSE),
	IF_MATCH("If-Match", REQUEST), IF_NONE_MATCH("If-None-Match", REQUEST),
	IF_MODIFIED_MATCH("If-Modified-Match", REQUEST), IF_UNMODIFIED_MATCH("If-Unmodified-Match", REQUEST),

	CONNECTION("Connection"), KEEP_ALIVE("Keep-Alive"),

	ACCEPT("Accept", REQUEST), ACCEPT_CHARSET("Accept-Charset", REQUEST),
	ACCEPT_ENCODING("Accept-Encoding", REQUEST), ACCEPT_LANGUAGE("Accept-Language", REQUEST),

	COOKIE("Cookie", REQUEST), SET_COOKIE("Set-Cookie", RESPONSE),

	CORS_ALLOW_ORIGIN("Access-Control-Allow-Origin", RESPONSE), CORS_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials", RESPONSE),
	CORS_ALLOW_HEADERS("Access-Control-Allow-Headers", RESPONSE), CORS_ALLOW_METHODS("Access-Control-Allow-Methods", RESPONSE),
	CORS_EXPOSE_HEADERS("Access-Control-Expose-Headers", RESPONSE), CORS_MAX_AGE("Access-Control-Max-Age", RESPONSE),
	CORS_REQUEST_HEADERS("Access-Control-Request-Headers", REQUEST), CORS_REQUEST_METHOD("Access-Control-Request-Method", REQUEST),
	ORIGIN("Origin", REQUEST),

	DNT("DNT", REQUEST), TK("Tk", RESPONSE),

	CONTENT_DISPOSITION("Content-Disposition"),

	CONTENT_LENGTH("Content-Length", ENTITY), CONTENT_TYPE("Content-Type", ENTITY), CONTENT_ENCODING("Content-Encoding", ENTITY),
	CONTENT_LANGUAGE("Content-Language", ENTITY), CONTENT_LOCATION("Content-Location", ENTITY),

	FORWARDED("Forwarded", REQUEST), VIA("Via"),

	LOCATION("Location", RESPONSE),

	FROM("From", REQUEST), HOST("Host", REQUEST), REFERER("Referer", REQUEST),
	REFERER_POLICY("Referer-Policy", RESPONSE), USER_AGENT("User-Agent", REQUEST),

	ALLOW("Allow", ENTITY), SERVER("Server", RESPONSE),

	ACCEPT_RANGES("Accept-Ranges", RESPONSE), RANGE("Range", REQUEST),
	IF_RANGE("If-Range", REQUEST), CONTENT_RANGE("Content-Range", RESPONSE),

	CONTENT_SECURITY_POLICY("Content-Security-Policy", RESPONSE), CONTENT_SECURITY_POLICY_REPORT_ONLY("Content-Security-Policy-Report-Only", RESPONSE),
	EXPECT_CT("Expect-CT", RESPONSE), FEATURE_POLICY("Feature-Policy", RESPONSE), PUBLIC_KEY_PINS("Public-Key-Pins", RESPONSE),
	PUBLIC_KEY_PINS_REPORT_ONLY("Public-Key-Pins-Report-Only", RESPONSE), STRICT_TRANSPORT_SECURITY("Strict-Transport-Security", RESPONSE),
	UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests", REQUEST),

	TRANSFER_ENCODING("Transfer-Encoding", RESPONSE), TE("TE", REQUEST), TRAILER("Trailer", RESPONSE),

	ALT_SVC("Alt-Svc"), DATE("Date"), LARGE_ALLOCATION("Large-Allocation", RESPONSE),
	RETRY_AFTER("Retry-After", RESPONSE), SERVER_TIMING("Server-Timing", RESPONSE), SOURCEMAP("SourceMap", RESPONSE);

	@Getter
	private String header;
	@Getter
	private HeaderType type;

	HttpHeader(String header, HeaderType type) {
		this.header = header;
		this.type = type;
	}

	HttpHeader(String header) {
		this.header = header;
		this.type = HeaderType.GENERAL;
	}

	public static HttpHeader fromHeaderString(String str) {
		for (HttpHeader hh : HttpHeader.values())
			if (hh.getHeader().equalsIgnoreCase(str)) return hh;
		return null;
	}

	public static List<HttpHeader> fromHeaderTypes(HeaderType... types) {
		List<HttpHeader> headers = new ArrayList<>();
		List<HeaderType> lstTypes = Arrays.asList(types);
		for (HttpHeader hh : HttpHeader.values())
			if (lstTypes.contains(hh.getType())) headers.add(hh);
		return headers;
	}

	public static String toHeaderListString(List<HttpHeader> headers) {
		return Joiner.on(",").join(headers.stream().map(HttpHeader::getHeader).collect(Collectors.toList()));
	}

}
