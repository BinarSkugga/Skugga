package com.binarskugga.skuggahttps.api.impl;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.ExceptionParser;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.enums.HeaderType;
import com.binarskugga.skuggahttps.api.enums.HttpHeader;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.exception.ReflectiveContructFailedException;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.undertow.server.HttpServerExchange;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.HeaderValues;
import io.undertow.util.HeaderValuesFactory;
import io.undertow.util.HttpString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class HttpSession {

	@Getter private HttpServerExchange exchange;
	@Getter private ServerProperties serverProperties;

	@Getter private Endpoint endpoint;
	@Getter private Object body;

	@Getter private Map<HttpHeader, HeaderValues> requestHeaders;
	@Getter private Map<HttpHeader, HeaderValues> responseHeaders;

	@Getter private Map<String, Cookie> requestCookies;
	@Getter private Map<String, Cookie> responseCookies;

	@Getter private Token token;
	private BodyParser parser;
	private ExceptionParser eParser;

	public HttpSession(HttpServerExchange exchange, ServerProperties properties) throws Exception {
		this.exchange = exchange;
		this.serverProperties = properties;

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

	public void setEndpoint(Endpoint endpoint) throws Exception {
		this.endpoint = endpoint;
		if(this.getRequestMethod().acceptBody() && endpoint != null) {
			byte[] data = ByteStreams.toByteArray(exchange.getInputStream());

			if(endpoint.getBodyType().equals(byte[].class)) {
				this.body = data;
			} else if(endpoint.getBodyType() instanceof ParameterizedType || endpoint.getBodyType() instanceof TypeVariable) {
				ParameterizedType pType;
				if(endpoint.getBodyType() instanceof TypeVariable) pType = ((ParameterizedType) (((TypeVariable) endpoint.getBodyType()).getBounds()[0]));
				else pType = (ParameterizedType) endpoint.getBodyType();
				this.body = HttpSession.this.getParser(endpoint)
						.toObject(this, new String(data), pType.getActualTypeArguments(), (Class) pType.getRawType());
			} else {
				this.body = HttpSession.this.getParser(endpoint)
						.toObject(this, new String(data, Charsets.UTF_8), new Type[] { endpoint.getBodyType() }, null);
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
		if(endpoint == null) return this.serverProperties.getDefaultContentType();
		return this.endpoint.getContentType() == null ? this.serverProperties.getDefaultContentType() : this.endpoint.getContentType();
	}

	public HttpMethod getRequestMethod() {
		return HttpMethod.fromMethodString(this.exchange.getRequestMethod().toString());
	}

	@SuppressWarnings("unchecked")
	public <T extends BodyParser> T getParser(Endpoint endpoint) {
		if(this.parser == null) {
			try {
				Class<? extends BodyParser> bodyParser = this.serverProperties.getParsers().get(this.getContentType());
				this.parser = ReflectionUtils.safeConstruct(bodyParser);
			} catch (ReflectiveContructFailedException e) {
				e.printStackTrace();
			}
		}
		return (T) this.parser;
	}

	@SuppressWarnings("unchecked")
	public <T extends ExceptionParser> T getExceptionParser(Endpoint endpoint) {
		if(this.eParser == null) {
			try {
				Class<? extends ExceptionParser> exceptionParser = this.serverProperties.getExceptionParsers().get(this.getContentType());
				this.eParser = ReflectionUtils.safeConstruct(exceptionParser);
			} catch (ReflectiveContructFailedException e) {
				e.printStackTrace();
			}
		}
		return (T) this.eParser;
	}

	@SuppressWarnings("unchecked")
	private <T extends Token> T createToken(Cookie cookie) throws SignatureException, ExpiredJwtException, MalformedJwtException, UnsupportedJwtException {
		Token token = ReflectionUtils.constructOrNull(this.serverProperties.getTokenClass());
		if(token == null) return null;

		token.parse(cookie.getValue());
		return (T) token;
	}

}
