package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.api.RequestHandler;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.enums.HttpStatus;
import com.binarskugga.skuggahttps.api.impl.ServerProperties;
import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.binarskugga.skuggahttps.util.CryptoUtils;
import com.binarskugga.skuggahttps.api.impl.endpoint.AbstractController;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.api.impl.endpoint.EndpointResolver;
import com.binarskugga.skuggahttps.api.exception.http.HttpException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.google.common.base.Charsets;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.Getter;

import javax.xml.ws.spi.http.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SkuggaHttpHandler extends LinkedList<RequestHandler> implements HttpHandler {

	@Getter private ServerProperties serverProperties;
	private final EndpointResolver endpointResolver;
	private Map<Class<? extends AbstractController>, AbstractController> controllers;

	public SkuggaHttpHandler() {
		CryptoUtils.createKeysIfNotExists("token-sign.key");

		this.serverProperties = new ServerProperties();
		this.endpointResolver = new EndpointResolver(this.serverProperties.getControllerPackage(), this.serverProperties.getRoot());
		this.controllers = new HashMap<>();
	}

	public String getHost() {
		return this.serverProperties.getIp();
	}

	public int getPort() {
		return this.serverProperties.getPort();
	}

	public String getRoot() {
		return this.serverProperties.getRoot();
	}

	@Override
	public void handleRequest(HttpServerExchange exchange) {
		if(exchange.isInIoThread()) {
			exchange.dispatch(() -> {
				exchange.startBlocking();
				HttpSession session = null;
				try {
					session = new HttpSession(exchange, this.serverProperties);

					Endpoint endpoint = this.endpointResolver.getEndpoint(exchange.getRequestPath(), HttpMethod.fromMethodString(exchange.getRequestMethod().toString()));
					session.setEndpoint(endpoint);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if(this.chain(session)) {
						byte[] output = this.invokeAction(session);
						if (output != null && output.length > 0) {
							session.getExchange().getOutputStream().write(output);
						} else {
							session.getExchange().setStatusCode(HttpStatus.NO_CONTENT.getCode());
						}

						this.chainPost(session);
					}
				} catch (HttpException e) {
					this.handleExceptionExchange(session, e, e.getStatus().getCode());
				} catch (Exception e) {
					Throwable cause = e.getCause();
					if(cause != null && HttpException.class.isAssignableFrom(cause.getClass())) {
						HttpException exception = (HttpException) e.getCause();
						this.handleExceptionExchange(session, exception, exception.getStatus().getCode());
					} else {
						this.handleExceptionExchange(session, e, HttpStatus.INTERNAL_ERROR.getCode());
					}
				} finally {
					session.apply();
					exchange.endExchange();
				}
			});
		}
	}

	public void append(RequestHandler handler) {
		this.add(handler);
	}

	public void prepend(RequestHandler handler) {
		this.add(0, handler);
	}

	private boolean chain(HttpSession session) throws Exception {
		for (RequestHandler handler : this) {
			boolean continueChain = handler.handle(session);
			if (!continueChain)
				return false;
		}
		return true;
	}

	private void chainPost(HttpSession session) throws Exception {
		for (RequestHandler handler : this)
			handler.handlePostRequest(session);
	}

	private void chainException(HttpSession session, Exception e) {
		for (RequestHandler handler : this)
			handler.handleException(session, e);
	}

	@SuppressWarnings("unchecked")
	private byte[] invokeAction(HttpSession session) throws Exception {
		Endpoint endpoint = session.getEndpoint();
		Method action = endpoint.getAction();

		AbstractController controller;
		if(this.controllers.containsKey(action.getDeclaringClass()))
			controller = this.controllers.get(action.getDeclaringClass());
		else {
			controller = (AbstractController) ReflectionUtils.safeConstruct(action.getDeclaringClass());
			this.controllers.putIfAbsent((Class<? extends AbstractController>) action.getDeclaringClass(), controller);
		}
		controller.setSession(session);

		List<Object> params = session.getEndpoint().getArguments(session);
		if (endpoint.getMethod().acceptBody() && endpoint.getBodyType() != null) params.add(0, session.getBody());
		Object result =  action.invoke(controller, params.toArray());

		if (endpoint.getReturnType().equals(byte[].class)) {
			return (byte[]) result;
		} else if (endpoint.getReturnType() instanceof ParameterizedType || endpoint.getReturnType() instanceof TypeVariable) {
			ParameterizedType pType;
			if (endpoint.getReturnType() instanceof TypeVariable)
				pType = ((ParameterizedType) (((TypeVariable) endpoint.getReturnType()).getBounds()[0]));
			else pType = (ParameterizedType) endpoint.getReturnType();
			return session.getParser(endpoint)
					.toString(session, result, pType.getActualTypeArguments(), (Class) pType.getRawType())
					.getBytes(Charsets.UTF_8);
		} else {
			return session.getParser(endpoint)
					.toString(session, result, new Type[] { endpoint.getReturnType() }, null)
					.getBytes(Charsets.UTF_8);
		}
	}

	private void handleExceptionExchange(HttpSession session, Exception e, int code) {
		try {
			session.getExchange().setStatusCode(code);
			if(session.getRequestMethod().acceptBody()) {
				session.getExchange().getOutputStream().write(session.getExceptionParser(session.getEndpoint())
						.toString(session, e).getBytes(Charsets.UTF_8));
			}
			this.chainException(session, e);
		} catch (IOException ignored) {}
	}

}
