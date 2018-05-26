package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.exception.*;
import com.google.common.collect.*;
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
import java.util.concurrent.atomic.*;
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

	private Map<String, Method> gets;
	private Map<String, Method> posts;

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
			Stream.of(controller.getDeclaredMethods()).forEach(method -> {
				if(method.isAnnotationPresent(Get.class))
					gets.putIfAbsent(controllerValue + "/" + method.getAnnotation(Get.class).value(), method);
				if(method.isAnnotationPresent(Post.class))
					posts.putIfAbsent(controllerValue + "/" + method.getAnnotation(Post.class).value(), method);
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
					Method action = HttpsServer.this.route(path, HttpsServer.this.gets);
					return (Response) action.invoke(action.getDeclaringClass().newInstance());
				} catch(Exception ignored) {}
				return Response.notfound();
			}

			@Override
			public Response onPost(Headers outHeaders, Headers inHeaders, String path, InputStream body) {
				try {
					String strBody = new String(ByteStreams.toByteArray(body));
					Method action = HttpsServer.this.route(path, HttpsServer.this.posts);
					logger.info(action.getName());
					return (Response) action.invoke(action.getDeclaringClass().newInstance());
				} catch(Exception ignored) {}
				return Response.notfound();
			}
		});
		this.server.setExecutor(this.executor);
		this.server.start();
		this.logger.info("Started HTTPS Server on " + this.host + ":" + this.port);
		if(this.executor != null)
			this.logger.debug("Using a thread pool of " + this.executorSize + " threads.");
		return this;
	}

	private Method route(String search, Map<String, Method> pool) {
		String[] searchParts = search.split("/");
		final AtomicReference<Method> chosen = new AtomicReference<>(null);
		pool.forEach((path, action) -> {
			String[] pathParts = path.split("/");
			if(searchParts.length != pathParts.length) return;

			boolean success = true;
			for(int i = 0; i < pathParts.length; i++) {
				if(!pathParts[i].equals(searchParts[i])) {
					success = false;
					break;
				}
			}
			if(!success) return;

			if(chosen.get() != null) throw new EndpointDeplicateException("Endpoint duplicate for " + search);
			else chosen.set(action);
		});
		return chosen.get();
	}

}
