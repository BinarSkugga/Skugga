package com.binarskugga.skuggahttps;

import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.enums.HttpHeader;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import lombok.Getter;
import org.reflections.Reflections;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerProperties {

	@Getter
	private static String protocol;
	@Getter
	private static String ip;
	@Getter
	private static int port;
	@Getter
	private static String root;

	@Getter
	private static String rootPackage;
	@Getter
	private static String controllerPackage;
	@Getter
	private static String modelPackage;
	@Getter
	private static Class<? extends Token> tokenClass;

	@Getter
	private static String contentType;
	@Getter
	private static String allowedOrigin;
	@Getter
	private static boolean allowedCredentials;
	@Getter
	private static List<HttpMethod> allowedMethods;
	@Getter
	private static List<HttpHeader> allowedHeaders;

	private static Properties properties;

	private ServerProperties() {
	}

	@SuppressWarnings("unchecked")
	public static void load(InputStream stream) {
		try {
			properties = new Properties();
			properties.load(stream);

			protocol = getString("server.protocol", "http");
			ip = getString("server.ip", "0.0.0.0");
			port = Integer.parseInt(getString("server.port", "8080"));
			root = getString("server.root", "api");

			rootPackage = getString("server.config.root-package", "");
			controllerPackage = getString("server.config.controller-package", "");
			modelPackage = getString("server.config.model-package", "");

			Reflections reflections = new Reflections(rootPackage);
			for (Class<? extends Token> token : reflections.getSubTypesOf(Token.class)) {
				if (token.isAnnotationPresent(com.binarskugga.skuggahttps.api.annotation.Token.class))
					tokenClass = token;
			}

			contentType = getString("server.config.content-type", "application/json");
			allowedOrigin = getString("server.cors.allowed-origin", "*");
			allowedCredentials = Boolean.parseBoolean(getString("server.cors.allowed-credentials", "false"));

			String methods = getString("server.cors.allowed-methods", "*");
			if (methods.equalsIgnoreCase("*")) allowedMethods = new ArrayList<>();
			else
				allowedMethods = Stream.of(methods.split(",")).map(s -> HttpMethod.fromMethodString(s.trim())).collect(Collectors.toList());

			String headers = getString("server.cors.allowed-headers", "*");
			if (headers.equalsIgnoreCase("*")) allowedHeaders = new ArrayList<>();
			else
				allowedHeaders = Stream.of(headers.split(",")).map(s -> HttpHeader.fromHeaderString(s.trim())).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static String getString(String key, Object def) {
		return (String) properties.getOrDefault(key, def);
	}

	public static Object getOrDefault(String key, Object def) {
		return properties.getOrDefault(key, def);
	}

}
