package com.binarskugga.skugga.api.impl.endpoint;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.ServerProperties;
import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.annotation.UseParser;
import com.binarskugga.skugga.api.enums.HttpMethod;
import com.binarskugga.skugga.api.exception.InvalidArgumentCountException;
import com.binarskugga.skugga.api.exception.InvalidArgumentException;
import com.binarskugga.skugga.api.exception.InvalidBodyException;
import com.binarskugga.skugga.api.impl.parse.BodyInformation;
import com.binarskugga.skugga.api.impl.parse.ParameterParsingHandler;
import com.binarskugga.skugga.util.EndpointUtils;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.*;

import java.io.InputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {

	@Getter @Setter private Method action;
	@Getter @Setter private Class<? extends AbstractController> controller;

	@Getter @Setter private HttpMethod method;
	@Getter @Setter private String route;
	@Setter private String contentType;

	@Getter @Setter private Type bodyType;
	@Getter @Setter private Type returnType;

	@Getter private Object body;

	public String getContentType() {
		return this.contentType == null ? ServerProperties.getContentType() : this.contentType;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArguments(HttpSession session) {
		String[] brokenEndpoint = EndpointUtils.sanitizePath(session.getEndpoint().getRoute()).split("/");
		String[] brokenRequest = EndpointUtils.sanitizePath(session.getExchange().getRequestPath()).split("/");

		List<Object> args = new ArrayList<>();

		int argumentCount = (int) Stream.of(brokenEndpoint).filter(b -> b.equals("$")).count();
		Parameter[] parameters = session.getEndpoint().getAction().getParameters();
		if (parameters.length > argumentCount) {
			if (parameters.length - argumentCount > 1)
				throw new InvalidArgumentCountException("Too much argument are specified for endpoint: '" + session.getEndpoint().getRoute() + "'");
			parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		}

		try {
			ParameterParsingHandler parsingHandler = ParameterParsingHandler.get();
			for (int i = 0, p = 0; i < brokenEndpoint.length; i++) {
				if (brokenEndpoint[i].equals("$")) {
					Parameter parameter = parameters[p++];
					ParameterParser parser = parsingHandler.getParser(parameter, PrimitivaReflection.getParamAnnotationOrNull(parameter, UseParser.class));
					args.add(parser.parse(parameter, brokenRequest[i]));
				}
			}
		} catch (Exception e) {
			throw new InvalidArgumentException();
		}

		return args;
	}

	@SuppressWarnings({"unchecked", "UnstableApiUsage"})
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
			throw new InvalidBodyException();
		}
	}

}
