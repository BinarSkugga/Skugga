package com.binarskugga.skuggahttps.api.impl;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.ExceptionParser;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.annotation.UseParser;
import com.binarskugga.skuggahttps.api.enums.HeaderType;
import com.binarskugga.skuggahttps.api.enums.HttpHeader;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.exception.ReflectiveContructFailedException;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.api.impl.parse.BodyParsingHandler;
import com.binarskugga.skuggahttps.api.impl.parse.ExceptionParsingHandler;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.undertow.server.HttpServerExchange;

import java.util.*;

import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.HeaderValues;
import io.undertow.util.HeaderValuesFactory;
import io.undertow.util.HttpString;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class HttpSession {

	@Getter private HttpServerExchange exchange;

	@Getter @Setter private Endpoint endpoint;

	@Getter private Map<HttpHeader, HeaderValues> requestHeaders;
	@Getter private Map<HttpHeader, HeaderValues> responseHeaders;

	@Getter private Map<String, Cookie> requestCookies;
	@Getter private Map<String, Cookie> responseCookies;

	@Getter private Token token;

	public HttpSession(HttpServerExchange exchange) throws Exception {
		this.exchange = exchange;

		this.responseHeaders = new HashMap<>();
		this.requestHeaders = new HashMap<>();
		for(HeaderValues hv : exchange.getRequestHeaders()) {
			HttpHeader header = HttpHeader.fromHeaderString(hv.getHeaderName().toString());
			if(header != null && header.getType() != HeaderType.RESPONSE)
				this.requestHeaders.put(header, hv);
		}

		this.responseCookies = exchange.getResponseCookies();
		this.requestCookies = exchange.getRequestCookies();

		if(this.requestCookies.containsKey("token")) {
			try {
				Cookie token = this.requestCookies.get("token");
				this.token = this.createToken(token);
			} catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException e) {
				this.token = null;

				Cookie tokenCookie = new CookieImpl("token", "");
				tokenCookie.setExpires(new Date(new Date().getTime() - 1000));
				tokenCookie.setHttpOnly(true);
				tokenCookie.setPath("/");
				exchange.setResponseCookie(tokenCookie);
			}
		}
	}

	public void apply() {
		this.responseHeaders.put(HttpHeader.CONTENT_TYPE, HeaderValuesFactory.create(HttpHeader.CONTENT_TYPE.getHeader(), this.getContentType()));
		for(Map.Entry<HttpHeader, HeaderValues> header : this.responseHeaders.entrySet()) {
			String[] values = header.getValue().toArray();
			exchange.getResponseHeaders().addAll(new HttpString(header.getKey().getHeader()), Arrays.asList(values));
		}

		for(Map.Entry<String, Cookie> cookie : this.responseCookies.entrySet()) {
			exchange.getResponseCookies().put(cookie.getKey(), cookie.getValue());
		}
	}

	public String getContentType() {
		if(endpoint == null) return ServerProperties.getContentType();
		return this.endpoint.getContentType();
	}

	public HttpMethod getRequestMethod() {
		return HttpMethod.fromMethodString(this.exchange.getRequestMethod().toString());
	}

	@SuppressWarnings("unchecked")
	public <T extends BodyParser> T getBodyParser() {
		if(this.endpoint != null) {
			return (T) new BodyParsingHandler().getParser(this.endpoint, ReflectionUtils.getMethodAnnotationOrNull(this.endpoint.getAction(), UseParser.class));
		} else {
			return (T) new ExceptionParsingHandler().getParsers().get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends ExceptionParser> T getExceptionParser() {
		if(this.endpoint != null) {
			return (T) new ExceptionParsingHandler().getParser(this.endpoint, ReflectionUtils.getMethodAnnotationOrNull(this.endpoint.getAction(), UseParser.class));
		} else {
			return (T) new ExceptionParsingHandler().getParsers().get(0);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Token> T createToken(Cookie cookie) throws SignatureException, ExpiredJwtException, MalformedJwtException, UnsupportedJwtException {
		Token token = ReflectionUtils.constructOrNull(ServerProperties.getTokenClass());
		if(token == null) return null;

		token.parse(cookie.getValue());
		return (T) token;
	}

}
