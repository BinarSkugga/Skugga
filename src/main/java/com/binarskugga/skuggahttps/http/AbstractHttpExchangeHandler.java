package com.binarskugga.skuggahttps.http;

import com.binarskugga.skuggahttps.annotation.Filter;
import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.controller.*;
import com.binarskugga.skuggahttps.data.*;
import com.binarskugga.skuggahttps.http.api.filter.impl.*;
import com.binarskugga.skuggahttps.response.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import com.sun.net.httpserver.*;
import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.http.api.filter.*;
import com.binarskugga.skuggahttps.http.exception.*;
import com.binarskugga.skuggahttps.http.path.*;
import com.binarskugga.skuggahttps.utils.*;
import org.reflections.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;
import java.util.zip.*;

import static com.binarskugga.skuggahttps.response.Response.*;

public abstract class AbstractHttpExchangeHandler<I extends Serializable> implements HttpHandler {

	private static final String CONTENT_TYPE = "Content-type";

	private Logger logger;
	private PropertiesConfiguration configuration;
	private EndpointResolver endpointResolver;

	private FilterChain filterChain;
	private Map<Class, AbstractController> controllers;

	public AbstractHttpExchangeHandler() {
		this.logger = Logger.getLogger(getClass().getName());
		this.configuration = HttpConfigProvider.get();

		this.controllers = new HashMap<>();
		this.createControllers(this.configuration.getString("server.package.controller").get());

		this.endpointResolver = new EndpointResolver(this, this.controllers.values());

		this.filterChain = new FilterChain();
		initiateFilters(HttpConfigProvider.get().getString("server.package.filter").get());
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		long time = System.currentTimeMillis();
		this.logger.info(String.format("%s %s · %s · %s", exchange.getProtocol(), exchange.getRequestMethod(), exchange.getRequestURI(), Thread.currentThread().getName()));

		HttpSession session = new HttpSession();
		session.setExchange(exchange);
		session.setConfig(HttpConfigProvider.get());

		OutputStream os = session.getExchange().getResponseBody();

		Headers inHeaders = session.getExchange().getRequestHeaders();
		Headers outHeaders = session.getExchange().getResponseHeaders();
		session.setCookies(this.parseCookies(inHeaders));

		boolean acceptGzip = inHeaders.get("Accept-Encoding").get(0).contains("gzip");
		if(acceptGzip) {
			outHeaders.add("Content-Encoding", "gzip");
		}

		outHeaders.add("Connection", session.getConfig().getString("headers.connection").orElse("keep-alive"));
		outHeaders.add(CONTENT_TYPE, session.getConfig().getString("headers.content-type").orElse("application/json;charset=UTF-8"));
		outHeaders.add("Server", session.getConfig().getString("server.name").orElse("SkuggaHttps"));
		outHeaders.add("Vary", session.getConfig().getString("headers.vary").orElse("Accept-Encoding"));
		outHeaders.add("Access-Control-Allow-Origin", session.getConfig().getString("headers.cors.access-control-allow-origin").orElse("*"));

		session.setResponseBody(Response.notfound());
		AbstractController controller = null;

		String path = session.getExchange().getRequestURI().getPath();

		session.setEndpoint(this.endpointResolver.getEndpoint(path, EndpointType.valueOf(session.getExchange().getRequestMethod())));
		if(session.getEndpoint() == null) {
			session.setResponseBody(Response.create(METHOD_NOT_ALLOWED, "This " + session.getExchange().getRequestMethod() + " method does not exist !"));
		} else {
			controller = this.controllers.get(session.getEndpoint().getAction().getDeclaringClass()).copy();
			controller.setSession(session);

			if(session.getEndpoint().getType().equals(EndpointType.POST)) {
				byte[] bytesBody = ByteStreams.toByteArray(session.getExchange().getRequestBody());
				session.setExtra("bytes", bytesBody);

				Method action = session.getEndpoint().getAction();
				Class<?> bodyType = action.getParameterTypes()[0];
				Object body = this.getResponseHandler().fromBytes(bodyType, bytesBody);

				session.setBody(body);
			}
			session.setArgs(this.endpointResolver.getArguments(session, path));

			try {
				filterChain.applyPre(session);

				session.setResponseBody(session.getEndpoint().getAction().invoke(controller, session.getArgs().values().toArray()));
				Class endpointReturnType = session.getEndpoint().getAction().getReturnType();
				if(HttpReturnable.class.isAssignableFrom(endpointReturnType)) {
					String ct = ((HttpReturnable) session.getResponseBody()).contentType();
					if(ct == null)
						outHeaders.set(CONTENT_TYPE, this.getResponseHandler().defaultContentType());
					else
						outHeaders.set(CONTENT_TYPE, ct);
				} else
					outHeaders.set(CONTENT_TYPE, this.getResponseHandler().defaultContentType());

				filterChain.applyPost(session);
			} catch(Exception e) {
				if(e.getCause() instanceof HTTPException || e instanceof HTTPException) {
					HTTPException httpException;
					if(e.getCause() instanceof HTTPException) httpException = (HTTPException) e.getCause();
					else httpException = (HTTPException) e;

					this.logger.warning(String.format("HTTP Error %d - %s", httpException.getStatus(), httpException.getMessage()));
					session.setResponseBody(Response.create(httpException.getStatus(), httpException.getMessage()));
				} else if(e.getCause() instanceof APIException || e instanceof APIException) {
					APIException apiException;
					if(e.getCause() instanceof APIException) apiException = (APIException) e.getCause();
					else apiException = (APIException) e;

					this.logger.warning(String.format("API Error %d - %s", apiException.getStatus(), apiException.getMessage()));
					session.setResponseBody(Response.create(apiException.getStatus(), apiException.getName(), apiException.getMessage()));

					if(apiException.getErrors() != null) ((Response)session.getResponseBody()).setErrors(apiException.getErrors());
				} else {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					for(String line : sw.toString().split("\n"))
						this.logger.severe(line);
					session.setResponseBody(Response.internalError(e.getClass().getName()));
				}
			}
		}

		int status = 200;
		if(session.getResponseBody() instanceof  Response)
			status = ((Response) session.getResponseBody()).getStatus();

		session.getExchange().sendResponseHeaders(status, 0);
		this.logger.config("================== " + (System.currentTimeMillis() - time) + "ms ==================");

		if(acceptGzip) {
			os = new GZIPOutputStream(os);
		}

		if(session.getResponseBody() instanceof HttpReturnable) {
			((HttpReturnable)session.getResponseBody()).write(os, this.getResponseHandler());
		} else if(outHeaders.get(CONTENT_TYPE).get(0).equals(this.getResponseHandler().defaultContentType())) {
			os.write(this.getResponseHandler().toBytes((Class) session.getEndpoint().getAction().getReturnType(), session.getResponseBody()));
		} else {
			Response.internalError("Can't parse object to bytes.").write(os, this.getResponseHandler());
		}

		os.close();
		session.getExchange().close();
	}

	private void createControllers(String controllerPackage) {
		Reflections reflections = new Reflections(controllerPackage);
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

		if(this.configuration.getBoolean("server.controller.meta").orElse(true))
			controllers.add(MetaController.class);
		controllers.stream().filter(controller -> AbstractController.class.isAssignableFrom(controller))
				.forEach(controller -> {
					try {
						this.controllers.put(controller, (AbstractController) controller.newInstance());
					} catch(Exception e) {
						logger.severe("Controllers need to have an empty constructor. (" + controller.getName() + ")");
					}
				});
	}

	private void initiateFilters(String filterPackage) {
		Reflections reflections = new Reflections(filterPackage);
		Set<Class<?>> filters = reflections.getTypesAnnotatedWith(Filter.class);
		if(getIdentityRepository() != null)
			filters.add(AuthFilter.class);
		filters.add(ValidationFilter.class);
		filters.stream().sorted((o1, o2) -> {
			int o1Priority = o1.getDeclaredAnnotation(Filter.class).value();
			int o2Priority = o2.getDeclaredAnnotation(Filter.class).value();
			return Integer.compare(o1Priority, o2Priority);
		}).forEach(filter -> {
			try {
				Class<? extends AbstractFilter> typedFilter = ((Class<? extends AbstractFilter>) filter);
				if(typedFilter.equals(AuthFilter.class)) {
					Constructor constructor = AuthFilter.class.getConstructor(DataRepository.class);
					this.filterChain.addFilter((AbstractFilter) constructor.newInstance(getIdentityRepository()));
				} else {
					this.filterChain.addFilter(typedFilter.newInstance());
				}
			} catch(Exception e) {
				logger.severe("Filters need to have an empty constructor. (" + filter.getName() + ")");
			}
		});
	}

	private Map<String, Cookie> parseCookies(Headers resquestHeaders) {
		if(resquestHeaders.get("Cookie") != null) {
			List<String> cookies = Lists.newArrayList(resquestHeaders.get("Cookie").get(0).split("; "));
			return cookies.stream().map(strCookie -> {
				String[] parts = strCookie.split("=");
				return new Cookie(parts[0], parts[1], "/");
			}).collect(Collectors.toMap(Cookie::getName, cookie -> cookie));
		} else
			return new HashMap<>();
	}

	public abstract HttpResponseHandler getResponseHandler();
	public abstract <Q, T extends GenericUser> DataRepository<Q, I, T> getIdentityRepository();
	public abstract I createID(String id);

}
