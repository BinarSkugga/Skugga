package com.binarskugga.skugga.api.impl.handler;

import com.binarskugga.skugga.ServerProperties;
import com.binarskugga.skugga.api.RequestHandler;
import com.binarskugga.skugga.api.annotation.Closed;
import com.binarskugga.skugga.api.enums.HeaderType;
import com.binarskugga.skugga.api.enums.HttpHeader;
import com.binarskugga.skugga.api.enums.HttpMethod;
import com.binarskugga.skugga.api.enums.HttpStatus;
import com.binarskugga.skugga.api.exception.ClosedEndpointException;
import com.binarskugga.skugga.api.exception.http.BadRequestException;
import com.binarskugga.skugga.api.exception.http.MethodNotAllowedException;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
import io.undertow.util.HeaderValues;
import io.undertow.util.HeaderValuesFactory;

import java.util.Arrays;
import java.util.Map;

public class AccessControlHandler implements RequestHandler {

	@Override
	public boolean handle(HttpSession session) throws RuntimeException {
		Map<HttpHeader, HeaderValues> resHeaders = session.getResponseHeaders();

		// NO ENDPOINT
		if (session.getEndpoint() == null)
			throw new BadRequestException("Endpoint doesn't exists.");

		// CLOSED ENDPOINT
		if (session.getEndpoint().getAction().isAnnotationPresent(Closed.class))
			throw new ClosedEndpointException();

		// CORS
		resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_ORIGIN,
				HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_ORIGIN.getHeader(), ServerProperties.getAllowedOrigin()));

		if (ServerProperties.isAllowedCredentials()) {
			resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_CREDENTIALS,
					HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_CREDENTIALS.getHeader(), ServerProperties.isAllowedCredentials()));
		}

		// UN-ALLOWED METHOD
		if (session.getRequestMethod() != HttpMethod.OPTIONS
				&& ServerProperties.getAllowedMethods().size() > 0 && !ServerProperties.getAllowedMethods().contains(session.getRequestMethod()))
			throw new MethodNotAllowedException("Http method " + session.getRequestMethod().name() + " not allowed.");

		if (session.getRequestMethod() != HttpMethod.OPTIONS && session.getRequestMethod() != session.getEndpoint().getMethod())
			throw new MethodNotAllowedException("Http method " + session.getRequestMethod().name() + " not allowed for this endpoint.");

		// OPTIONS RESPONSE
		if (session.getRequestMethod() == HttpMethod.OPTIONS) {
			if (ServerProperties.getAllowedMethods().size() > 0) {
				resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_METHODS,
						HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_METHODS.getHeader(),
								HttpMethod.toHeaderListString(ServerProperties.getAllowedMethods())));
			} else {
				resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_METHODS,
						HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_METHODS.getHeader(),
								HttpMethod.toHeaderListString(Arrays.asList(HttpMethod.values()))));
			}

			if (ServerProperties.getAllowedHeaders().size() > 0) {
				resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_HEADERS,
						HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_METHODS.getHeader(),
								HttpHeader.toHeaderListString(ServerProperties.getAllowedHeaders())));
			} else {
				resHeaders.putIfAbsent(HttpHeader.CORS_ALLOW_HEADERS,
						HeaderValuesFactory.create(HttpHeader.CORS_ALLOW_METHODS.getHeader(),
								HttpHeader.toHeaderListString(HttpHeader.fromHeaderTypes(HeaderType.REQUEST, HeaderType.GENERAL, HeaderType.ENTITY))));
			}

			session.getExchange().setStatusCode(HttpStatus.NO_CONTENT.getCode());
			return false;
		}
		return true;
	}

}
