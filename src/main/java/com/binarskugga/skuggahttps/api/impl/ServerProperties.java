package com.binarskugga.skuggahttps.api.impl;

import com.binarskugga.skuggahttps.api.BodyParser;
import com.binarskugga.skuggahttps.api.ExceptionParser;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.annotation.ContentType;
import com.binarskugga.skuggahttps.api.enums.HttpHeader;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.exception.NoDefaultBodyParserException;
import com.binarskugga.skuggahttps.api.exception.NoDefaultExceptionParserException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.binarskugga.skuggahttps.util.ResourceUtils;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerProperties {

	@Getter private String protocol;
	@Getter private String ip;
	@Getter private int port;
	@Getter private String root;

	@Getter private String controllerPackage;
	@Getter private Class<? extends Token> tokenClass;

	@Getter private String defaultContentType;
	@Getter private Map<String, Class<? extends BodyParser>> parsers;
	@Getter private Map<String, Class<? extends ExceptionParser>> exceptionParsers;

	@Getter private String allowedOrigin;
	@Getter private boolean allowedCredentials;
	@Getter private List<HttpMethod> allowedMethods;
	@Getter private List<HttpHeader> allowedHeaders;

	private Properties properties;

	@SuppressWarnings("unchecked")
	public ServerProperties() {
		try {
			this.properties = new Properties();
			this.properties.load(ResourceUtils.getCompiledResource("server.properties"));

			this.protocol = (String) properties.getOrDefault("server.protocol", "http");
			this.ip = (String) properties.getOrDefault("server.ip", "0.0.0.0");
			this.port = Integer.parseInt((String) properties.getOrDefault("server.port", "8080"));
			this.root = (String) properties.getOrDefault("server.root", "api");

			this.controllerPackage = (String) properties.getOrDefault("server.config.controller-package", "");
			this.tokenClass = (Class<? extends Token>) Class.forName((String) properties.getOrDefault("server.config.token", ""));
			this.defaultContentType = (String) properties.getOrDefault("server.config.default-content-type", "application/json");

			this.parsers = new HashMap<>();
			List<Class<? extends BodyParser>> parsersClass = Arrays.stream(((String)properties.getOrDefault("server.config.parsers", "")).split(","))
					.map(ReflectionUtils::forNameOrNull).map(c -> (Class<? extends BodyParser>)c).collect(Collectors.toList());
			for(Class<? extends BodyParser> clazz : parsersClass) {
				ContentType contentType = ReflectionUtils.getClassAnnotationOrNull(clazz, ContentType.class);
				String strCT = contentType == null ? this.defaultContentType : contentType.value();
				if(!this.parsers.containsKey(strCT)) this.parsers.put(strCT, clazz);
			}
			if(this.getBodyParserClass(this.defaultContentType) == null)
				throw new NoDefaultBodyParserException();

			this.exceptionParsers = new HashMap<>();
			List<Class<? extends ExceptionParser>> exceptionParsersClass = Arrays.stream(((String)properties.getOrDefault("server.config.exception-parsers", "")).split(","))
					.map(ReflectionUtils::forNameOrNull).map(c -> (Class<? extends ExceptionParser>)c).collect(Collectors.toList());
			for(Class<? extends ExceptionParser> clazz : exceptionParsersClass) {
				ContentType contentType = ReflectionUtils.getClassAnnotationOrNull(clazz, ContentType.class);
				String strCT = contentType == null ? this.defaultContentType : contentType.value();
				if(!this.exceptionParsers.containsKey(strCT)) this.exceptionParsers.put(strCT, clazz);
			}
			if(this.getExceptionParserClass(this.defaultContentType) == null)
				throw new NoDefaultExceptionParserException();

			this.allowedOrigin = (String) properties.getOrDefault("server.cors.allowed-origin", "*");
			this.allowedCredentials = Boolean.parseBoolean((String) properties.getOrDefault("server.cors.allowed-credentials", "false"));

			String methods = (String) properties.getOrDefault("server.cors.allowed-methods", "*");
			if(methods.equalsIgnoreCase("*")) this.allowedMethods = new ArrayList<>();
			else this.allowedMethods = Stream.of(methods.split(",")).map(s -> HttpMethod.fromMethodString(s.trim())).collect(Collectors.toList());

			String headers = (String) properties.getOrDefault("server.cors.allowed-headers", "*");
			if(headers.equalsIgnoreCase("*")) this.allowedHeaders = new ArrayList<>();
			else this.allowedHeaders = Stream.of(headers.split(",")).map(s -> HttpHeader.fromHeaderString(s.trim())).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public Class<? extends BodyParser> getBodyParserClass(String contentType) {
		return this.parsers.containsKey(contentType) ? this.parsers.get(contentType) : this.parsers.get(this.defaultContentType);
	}

	public Class<? extends ExceptionParser> getExceptionParserClass(String contentType) {
		return this.exceptionParsers.containsKey(contentType) ? this.exceptionParsers.get(contentType) : this.exceptionParsers.get(this.defaultContentType);
	}

	public Object getOrDefault(String key, Object def) {
		return this.properties.getOrDefault(key, def);
	}

}
