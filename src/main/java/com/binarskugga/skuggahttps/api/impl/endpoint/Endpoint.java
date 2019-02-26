package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.api.annotation.UseParser;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.exception.InvalidArgumentCountException;
import com.binarskugga.skuggahttps.api.impl.HttpSession;

import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import com.binarskugga.skuggahttps.api.impl.ServerProperties;
import com.binarskugga.skuggahttps.api.impl.parse.BodyInformation;
import com.binarskugga.skuggahttps.api.impl.parse.ParameterParsingHandler;
import com.binarskugga.skuggahttps.util.EndpointUtils;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class Endpoint {

	@Getter @Setter private Method action;
	@Getter @Setter private HttpMethod method;
	@Getter @Setter private String route;
	@Setter private String contentType;

	@Getter @Setter private Type bodyType;
	@Getter @Setter private Type returnType;

	@Getter private Object body;

	public String getContentType() {
		return this.contentType == null ? ServerProperties.getContentType() : this.contentType;
	}

	public List<Object> getArguments(HttpSession session) {
		String[] brokenEndpoint = EndpointUtils.sanitizePath(session.getEndpoint().getRoute()).split("/");
		String[] brokenRequest = EndpointUtils.sanitizePath(session.getExchange().getRequestPath()).split("/");

		List<Object> args = new ArrayList<>();

		int argumentCount = (int) Stream.of(brokenEndpoint).filter(b -> b.equals("$")).count();
		Parameter[] parameters = session.getEndpoint().getAction().getParameters();
		if(parameters.length > argumentCount) {
			if(parameters.length - argumentCount > 1)
				throw new InvalidArgumentCountException("Too much argument are specified for endpoint: '" + session.getEndpoint().getRoute() + "'");
			parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		}

		ParameterParsingHandler parsingHandler = new ParameterParsingHandler();
		for(int i = 0, p = 0; i < brokenEndpoint.length; i++) {
			if(brokenEndpoint[i].equals("$")) {
				Parameter parameter = parameters[p++];
				ParameterParser parser = parsingHandler.getParser(parameter, ReflectionUtils.getParamAnnotationOrNull(parameter, UseParser.class));
				args.add(parser.parse(parameter, brokenRequest[i]));
			}
		}

		return args;
	}

	@SuppressWarnings("unchecked")
	public void setBody(InputStream stream, HttpSession session) {
		try {
			byte[] data = ByteStreams.toByteArray(stream);

			if (this.getBodyType().equals(byte[].class)) {
				this.body = data;
			} else if (this.getBodyType() instanceof ParameterizedType || this.getBodyType() instanceof TypeVariable) {
				ParameterizedType pType;
				if (this.getBodyType() instanceof TypeVariable)
					pType = ((ParameterizedType) (((TypeVariable) this.getBodyType()).getBounds()[0]));
				else pType = (ParameterizedType) this.getBodyType();

				BodyInformation information = new BodyInformation(pType.getActualTypeArguments(), (Class) pType.getRawType(), session);
				this.body = session.getBodyParser().parse(information, new String(data));
			} else {
				BodyInformation information = new BodyInformation(new Type[]{this.getBodyType()}, null, session);
				this.body = session.getBodyParser().parse(information, new String(data, Charsets.UTF_8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
