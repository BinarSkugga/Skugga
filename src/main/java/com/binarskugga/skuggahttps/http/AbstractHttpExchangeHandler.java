package com.binarskugga.skuggahttps.http;

import com.binarskugga.skuggahttps.annotation.Filter;
import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.data.*;
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

import static com.binarskugga.skuggahttps.http.Response.*;

public abstract class AbstractHttpExchangeHandler<I extends Serializable> implements HttpHandler {

	private Logger logger;
	private PropertiesConfiguration configuration;
	private EndpointResolver endpointResolver;

	private List<Class<? extends AbstractFilter>> filters;

	public AbstractHttpExchangeHandler() {
		this.logger = Logger.getLogger(getClass().getName());
		this.configuration = HttpConfigProvider.get();

		Reflections ref = new Reflections(this.configuration.getString("server.package.controller").get());
		this.endpointResolver = new EndpointResolver(this, ref.getSubTypesOf(AbstractController.class));

		this.filters = new ArrayList<>();
		initiateFilters(HttpConfigProvider.get().getString("server.package.filter").get());
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		long time = System.currentTimeMillis();
		this.logger.info(String.format("%s %s · %s · %s", exchange.getProtocol(), exchange.getRequestMethod(), exchange.getRequestURI(), Thread.currentThread().getName()));

		HttpSession session = new HttpSession();
		session.setExchange(exchange);
		session.setConfig(HttpConfigProvider.get());

		FilterChain chain = new FilterChain();
		for(Class<? extends AbstractFilter> clazz : this.filters) {
			try {
				if(clazz.equals(AuthFilter.class)) {
					Constructor constructor = AuthFilter.class.getConstructor(DataRepository.class);
					chain.addFilter((AbstractFilter) constructor.newInstance(getIdentityRepository()));
				} else {
					chain.addFilter(clazz.newInstance());
				}
			} catch(Exception e) {
				this.logger.log(Level.SEVERE, "", e.getCause());
			}
		}

		OutputStream os = session.getExchange().getResponseBody();

		Headers inHeaders = session.getExchange().getRequestHeaders();
		Headers outHeaders = session.getExchange().getResponseHeaders();
		session.setCookies(this.parseCookies(inHeaders));

		boolean acceptGzip = inHeaders.get("Accept-Encoding").get(0).contains("gzip");
		if(acceptGzip) {
			outHeaders.add("Content-Encoding", "gzip");
		}

		outHeaders.add("Connection", session.getConfig().getString("headers.connection").orElse("keep-alive"));
		outHeaders.add("Content-Type", session.getConfig().getString("headers.content-type").orElse("application/json;charset=UTF-8"));
		outHeaders.add("Server", session.getConfig().getString("headers.server").orElse("SkuggaHttps"));
		outHeaders.add("Vary", session.getConfig().getString("headers.vary").orElse("Accept-Encoding"));
		outHeaders.add("Access-Control-Allow-Origin", session.getConfig().getString("headers.cors.access-control-allow-origin").orElse("*"));

		session.setResponse(Response.notfound());
		AbstractController controller = null;

		String path = session.getExchange().getRequestURI().getPath();

		session.setEndpoint(this.endpointResolver.getEndpoint(path, EndpointType.valueOf(session.getExchange().getRequestMethod())));
		if(session.getEndpoint() == null) {
			session.setResponse(Response.create(METHOD_NOT_ALLOWED, "This " + session.getExchange().getRequestMethod() + " method does not exist !"));
		} else {
			try {
				controller = (AbstractController) session.getEndpoint().getAction().getDeclaringClass().newInstance();
				controller.setSession(session);
			} catch(Exception e) {
				logger.log(Level.SEVERE, "Controllers need to have an empty constructor.", e);
			}

			if(session.getEndpoint().getAction().isAnnotationPresent(ContentType.class)) {
				outHeaders.set("Content-Type", session.getEndpoint().getAction().getDeclaredAnnotation(ContentType.class).value() + ";charset=UTF-8");
			}

			if(session.getEndpoint().getType().equals(EndpointType.POST)) {
				String strBody = new String(ByteStreams.toByteArray(session.getExchange().getRequestBody()));
				session.setExtra("str-body", strBody);

				Method action = session.getEndpoint().getAction();
				Class<?> bodyType = action.getParameterTypes()[0];
				Object body = this.getJsonHandler().fromJson(bodyType, strBody);

				session.setBody(body);
			}
			session.setArgs(this.endpointResolver.getArguments(session, path));

			try {
				chain.applyPre(session);

				Object obj = session.getEndpoint().getAction().invoke(controller, session.getArgs().values().toArray());
				Class endpointReturnType = session.getEndpoint().getAction().getReturnType();
				if(endpointReturnType.equals(Response.class)) session.setResponse((Response) obj);
				else session.setResponse(Response.ok(this.getJsonHandler().toJson(endpointReturnType, obj)));

				chain.applyPost(session);
			} catch(Exception e) {
				if(e.getCause() instanceof HTTPException || e instanceof HTTPException) {
					HTTPException httpException;
					if(e.getCause() instanceof HTTPException) httpException = (HTTPException) e.getCause();
					else httpException = (HTTPException) e;

					this.logger.warning(String.format("HTTP error %d - %s", httpException.getStatus(), httpException.getMessage()));
					session.setResponse(Response.create(httpException.getStatus(), httpException.getMessage()));
				} else if(e.getCause() instanceof APIException || e instanceof APIException) {
					APIException apiException;
					if(e.getCause() instanceof APIException) apiException = (APIException) e.getCause();
					else apiException = (APIException) e;

					this.logger.warning(String.format("HTTP %d - %s", apiException.getStatus(), apiException.getMessage()));
					session.setResponse(Response.create(apiException.getStatus(), apiException.getName(), apiException.getMessage()));
				} else {
					this.logger.log(Level.SEVERE, String.format("Server error"), e);
					session.setResponse(Response.internalError(e.getMessage()));
				}
			}
		}

		session.getExchange().sendResponseHeaders(session.getResponse().getStatus(), 0);
		this.logger.config("================== " + (System.currentTimeMillis() - time) + "ms ==================");

		if(acceptGzip) {
			os = new GZIPOutputStream(os);
		}

		if(session.getResponse().getBody() != null) {
			String rp;
			if(session.getResponse().getStatus() > 300) {
				rp = this.getJsonHandler().toJson(Response.class, session.getResponse());
			} else {
				rp = session.getResponse().getBody();
			}

			os.write(rp.getBytes(Charsets.UTF_8));
		} else {
			os.write(session.getResponse().getStatus());
		}

		os.close();
		session.getExchange().close();
	}

	private void initiateFilters(String filterPackage) {
		Reflections reflections = new Reflections(filterPackage);
		Set<Class<?>> filters = reflections.getTypesAnnotatedWith(Filter.class);
		if(getIdentityRepository() != null)
			filters.add(AuthFilter.class);
		filters.stream().sorted((o1, o2) -> {
			int o1Priority = o1.getDeclaredAnnotation(Filter.class).value();
			int o2Priority = o2.getDeclaredAnnotation(Filter.class).value();
			return Integer.compare(o1Priority, o2Priority);
		}).forEach(filter -> AbstractHttpExchangeHandler.this.filters.add((Class<? extends AbstractFilter>) filter));
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

	public abstract HttpJsonHandler getJsonHandler();
	public abstract <Q, T extends Identifiable> DataRepository<Q, I, T> getIdentityRepository();
	public abstract I createID(String id);

}
