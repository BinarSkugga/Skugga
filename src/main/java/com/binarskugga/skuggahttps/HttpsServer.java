package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.exception.*;
import com.binarskugga.skuggahttps.parse.*;
import com.google.common.io.*;
import com.sun.net.httpserver.*;
import org.reflections.*;
import org.slf4j.*;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static com.binarskugga.skuggahttps.Response.*;

public class HttpsServer {

	private int port;
	private String host;

	private KeyStore keyStore;
	private com.sun.net.httpserver.HttpsServer server;
	private SSLContext sslContext;

	private ExecutorService executor = null;
	private int executorSize;

	private Map<String, ParsedAction> gets;
	private Map<String, ParsedAction> posts;

	private PropertiesConfiguration configuration;
	private Logger logger;

	public HttpsServer(String host, int port) {
		this.port = port;
		this.host = host;

		this.logger = LoggerFactory.getLogger(HttpsServer.class);
		this.configuration = new PropertiesConfiguration("http.properties");

		this.gets = new HashMap<>();
		this.posts = new HashMap<>();

		try {
			this.server = com.sun.net.httpserver.HttpsServer.create(new InetSocketAddress(host, port), 1);
			this.sslContext = SSLContext.getInstance("TLS");
		} catch(Exception e) {
			// TODO: Define this
			e.printStackTrace();
		}
	}

	public HttpsServer controllers(String pack) {
		Reflections ref = new Reflections(pack);
		ref.getTypesAnnotatedWith(Controller.class).forEach(controller -> {
			Controller controllerAnnotation = controller.getAnnotation(Controller.class);
			String controllerValue = this.configuration.getString("settings.root") + controllerAnnotation.value();
			List<String> parsed = new ArrayList<>();
			Stream.of(controller.getDeclaredMethods()).forEach(method -> {
				if(method.isAnnotationPresent(Get.class)) {
					String endpoint = method.getAnnotation(Get.class).value();
					String parsedPath = "GET:" + endpoint.replaceAll("\\{[A-z]+\\}", "{}");
					if(parsed.contains(parsedPath))
						throw new EndpointDuplicateException("The endpoint " + endpoint + " is duplicated as a GET in the controller " + controller.getSimpleName() + ".");
					String fullroute = controllerValue + "/" + endpoint;
					gets.putIfAbsent(fullroute, HttpsServer.this.parseRoute(fullroute, method));
					parsed.add(parsedPath);
				} else if(method.isAnnotationPresent(Post.class)) {
					String endpoint = method.getAnnotation(Post.class).value();
					String parsedPath = "POST:" + endpoint.replaceAll("\\{[A-z]+\\}", "{}");
					if(parsed.contains(parsedPath))
						throw new EndpointDuplicateException("The endpoint " + endpoint + " is duplicated as a POST in the controller " + controller.getSimpleName() + ".");
					String fullroute = controllerValue + "/" + endpoint;
					posts.putIfAbsent(fullroute, HttpsServer.this.parseRoute(fullroute, method));
					parsed.add(parsedPath);
				}
			});
		});
		return this;
	}

	public HttpsServer ssl(String resourceName, String password) {
		try {
			char[] passArray = password.toCharArray();
			this.keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream stream = new FileInputStream(Resources.getResource(resourceName).getPath());
			this.keyStore.load(stream, passArray);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(this.keyStore, passArray);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(this.keyStore);

			this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			this.server.setHttpsConfigurator(new HttpsConfigurator(this.sslContext) {
				@Override
				public void configure(HttpsParameters params) {
					try {
						SSLContext context = SSLContext.getDefault();
						SSLEngine engine = context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());

						SSLParameters sslParams = context.getDefaultSSLParameters();
						params.setSSLParameters(sslParams);
					} catch(NoSuchAlgorithmException e) {
						// TODO: Define this
						e.printStackTrace();
					}
				}
			});
		} catch(Exception e) {
			// TODO: Define this
			e.printStackTrace();
		}
		return this;
	}

	public HttpsServer threaded(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
		this.executorSize = threads;
		return this;
	}

	public HttpsServer start() {
		this.server.createContext(this.configuration.getString("settings.root"), new AbstractHttpExchangeHandler() {
			@Override
			public Response onGet(Headers outHeaders, Headers inHeaders, String path) {
				try {
					ParsedAction parsed = HttpsServer.this.route(path, HttpsServer.this.gets);
					Method action = parsed.getAction();
					return (Response) action.invoke(action.getDeclaringClass().newInstance(), parsed.args().toArray());
				} catch(UndefinedRouteException e) {
					return Response.notfound();
				} catch(Exception ignored) {}
				return Response.create(METHOD_NOT_ALLOWED, "{\"value\":\"salut\"}");
			}

			@Override
			public Response onPost(Headers outHeaders, Headers inHeaders, String path, InputStream body) {
				try {
					String strBody = new String(ByteStreams.toByteArray(body));
					ParsedAction parsed = HttpsServer.this.route(path, HttpsServer.this.posts);
					Method action = parsed.getAction();
					List<Object> args = parsed.args();
					args.add(strBody);
					return (Response) action.invoke(action.getDeclaringClass().newInstance(), args.toArray());
				} catch(Exception ignored) {}
				return Response.create(METHOD_NOT_ALLOWED, "{\"value\":\"salut\"}");
			}
		});
		this.server.setExecutor(this.executor);
		this.server.start();
		this.logger.info("Started HTTPS Server on " + this.host + ":" + this.port);
		if(this.executor != null)
			this.logger.debug("Using a thread pool of " + this.executorSize + " threads.");
		return this;
	}

	private ParsedAction parseRoute(String path, Method action) {
		ParsedAction result = new ParsedAction();
		String[] pathParts = path.split("/");

		int paramIndex = 0;
		for(String pathPart : pathParts) {
			if(pathPart.startsWith("{") && pathPart.endsWith("}")) {
				String cleaned = pathPart.substring(1, pathPart.length() - 1);
				URLParamParser parser = HttpsServer.this.parser(cleaned);
				if(parser == null) throw new WrongUrlFormatException();
				result.addParam(paramIndex++, parser, null);
			}
		}
		result.setAction(action);
		return result;
	}

	private ParsedAction route(String search, Map<String, ParsedAction> pool) {
		ParsedAction chosen = null;
		String[] searchParts = search.substring(1).split("/");
		for(Map.Entry<String, ParsedAction> entry : pool.entrySet()) {
			chosen = entry.getValue();
			String[] pathParts = entry.getKey().substring(1).split("/");
			if(searchParts.length != pathParts.length) continue;

			int paramIndex = 0;
			for(int i = 0; i < pathParts.length; i++) {
				if(pathParts[i].startsWith("{") && pathParts[i].endsWith("}")) {
					chosen.setParam(paramIndex++, searchParts[i]);
				} else if(!pathParts[i].equals(searchParts[i])) {
					chosen = null;
					break;
				}
			}
			if(chosen != null) break;
		}
		if(chosen == null)
			throw new UndefinedRouteException();
		return chosen;
	}

	private URLParamParser parser(String type) {
		switch(type){
			case "int":
				return new IntParamParser();
			case "long":
				return new LongParamParser();
			case "uuid":
				return new UUIDParamParser();
			case "string":
				return new StringParamParser();
		}
		return null;
	}

}
