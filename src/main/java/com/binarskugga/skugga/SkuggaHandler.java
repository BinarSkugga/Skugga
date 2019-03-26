package com.binarskugga.skugga;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.skugga.api.DataConnector;
import com.binarskugga.skugga.api.RequestHandler;
import com.binarskugga.skugga.api.enums.HttpMethod;
import com.binarskugga.skugga.api.enums.HttpStatus;
import com.binarskugga.skugga.api.exception.http.HttpException;
import com.binarskugga.skugga.api.impl.endpoint.AbstractController;
import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.endpoint.EndpointResolver;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
import com.binarskugga.skugga.api.impl.parse.*;
import com.binarskugga.skugga.util.CryptoUtils;
import com.binarskugga.skugga.util.ResourceUtils;
import com.google.common.base.Charsets;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SkuggaHandler extends LinkedList<RequestHandler> implements HttpHandler {

	private EndpointResolver endpointResolver;
	private Map<Class<? extends AbstractController>, AbstractController> controllers;
	private DataConnector connector;

	public SkuggaHandler(DataConnector connector) {
		try {
			CryptoUtils.createKeysIfNotExists("token-sign");
			ServerProperties.load(ResourceUtils.getServerProperties());

			BodyParsingHandler.get();
			ExceptionParsingHandler.get();
			FieldParsingHandler.get();
			ParameterParsingHandler.get();

			this.endpointResolver = new EndpointResolver(ServerProperties.getControllerPackage(), ServerProperties.getRoot());
			this.controllers = new HashMap<>();

			if(connector != null) {
				this.connector = connector;
				connector.connect(ServerProperties.getModelPackage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public SkuggaHandler() {
		this(null);
	}

	@Override
	public void handleRequest(HttpServerExchange exchange) {
		if (exchange.isInIoThread()) {
			exchange.dispatch(() -> {
				exchange.startBlocking();
				HttpSession session = null;
				try {
					session = new HttpSession(exchange);
					session.setEndpoint(this.endpointResolver.getEndpoint(exchange.getRequestPath(), HttpMethod.fromMethodString(exchange.getRequestMethod().toString())));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}

				try {
					if (session.getRequestMethod().acceptBody() && session.getEndpoint() != null)
						session.getEndpoint().setBody(exchange.getInputStream(), session);
					if (this.chain(session)) {
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
					if (cause != null && HttpException.class.isAssignableFrom(cause.getClass())) {
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

	public SkuggaHandler append(RequestHandler handler) {
		this.add(handler);
		return this;
	}

	public SkuggaHandler prepend(RequestHandler handler) {
		this.add(0, handler);
		return this;
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
		if (this.controllers.containsKey(endpoint.getController()))
			controller = this.controllers.get(endpoint.getController());
		else {
			controller = Primitiva.Reflection.ofType(endpoint.getController()).safeCreate();
			this.controllers.putIfAbsent(endpoint.getController(), controller);
		}
		controller.setSession(session);

		List<Object> params = session.getEndpoint().getArguments(session);
		if (endpoint.getMethod().acceptBody() && endpoint.getBodyType() != null) params.add(0, endpoint.getBody());
		Object result = action.invoke(controller, params.toArray());

		if(!(endpoint.getReturnType() instanceof ParameterizedType) && !(endpoint.getReturnType() instanceof TypeVariable)) {
			if (ClassTools.of(endpoint.getReturnType()).isOneOf(Byte[].class, byte[].class)) {
				if (endpoint.getReturnType().equals(Byte[].class))
					return ArrayUtils.toPrimitive((Byte[]) result);
				return (byte[]) result;
			} else if (ClassTools.of(endpoint.getReturnType()).isOneOf(Byte.class, byte.class)) {
				return new byte[]{(byte) result};
			} else if (endpoint.getReturnType().equals(void.class)) {
				return new byte[0];
			} else {
				BodyInformation information = new BodyInformation(new Type[]{endpoint.getReturnType()}, null, session);
				return ((String) session.getBodyParser().unparse(information, result)).getBytes(Charsets.UTF_8);
			}
		} else {
			ParameterizedType pType;
			if (endpoint.getReturnType() instanceof TypeVariable)
				pType = ((ParameterizedType) (((TypeVariable) endpoint.getReturnType()).getBounds()[0]));
			else pType = (ParameterizedType) endpoint.getReturnType();

			BodyInformation information = new BodyInformation(pType.getActualTypeArguments(), (Class) pType.getRawType(), session);
			return ((String) session.getBodyParser().unparse(information, result)).getBytes(Charsets.UTF_8);
		}
	}

	@SuppressWarnings("unchecked")
	private void handleExceptionExchange(HttpSession session, Exception e, int code) {
		try {
			session.getExchange().setStatusCode(code);
			session.getExchange().getOutputStream().write(((String) session.getExceptionParser().unparse(session, e)).getBytes(Charsets.UTF_8));
			this.chainException(session, e);
		} catch (Exception ignored) {
			// We have to believe it works here because this is the last net.
		}
	}

}
