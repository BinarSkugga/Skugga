package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.api.ParameterParser;
import com.binarskugga.skuggahttps.api.annotation.ParamParser;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.api.exception.InvalidArgumentCountException;
import com.binarskugga.skuggahttps.api.exception.InvalidArgumentException;
import com.binarskugga.skuggahttps.api.impl.HttpSession;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import com.binarskugga.skuggahttps.util.EndpointUtils;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class Endpoint {

	@Getter @Setter private Method action;
	@Getter @Setter private HttpMethod method;
	@Getter @Setter private String route;
	@Getter @Setter private String contentType;

	@Getter @Setter private Type bodyType;
	@Getter @Setter private Type returnType;

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

		for(int i = 0, p = 0; i < brokenEndpoint.length; i++) {
			if(brokenEndpoint[i].equals("$")) {
				args.add(parseArgument(parameters[p++], brokenRequest[i]));
			}
		}

		return args;
	}

	private Object parseArgument(Parameter parameter, String argument) {
		ParamParser options = ReflectionUtils.getParamAnnotationOrNull(parameter, ParamParser.class);
		if(options == null) {
			try {
				if (parameter.getType().equals(String.class))
					return argument;
				else if(parameter.getType().equals(Class.class))
					return ReflectionUtils.forNameOrNull(argument);
				else if(parameter.getType().equals(UUID.class))
					return UUID.fromString(argument);
				else if(parameter.getType().equals(ObjectId.class))
					return new ObjectId(argument);
				else if (ReflectionUtils.isPrimitiveOrBoxed(parameter.getType()))
					return ReflectionUtils.stringToPrimitive(argument, parameter.getType());
				else if (ReflectionUtils.getInnerArrayType(parameter.getType()).equals(String.class))
					return Arrays.asList(argument.split(","));
				else if (ReflectionUtils.isPrimitiveArrayOrBoxed(parameter.getType()))
					return ReflectionUtils.stringToPrimitiveArray(argument, ",", parameter.getType());
				else if (Collection.class.isAssignableFrom(parameter.getType())) {
					Class inner = (Class) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
					if (ReflectionUtils.isBoxedPrimitive(inner))
						return ReflectionUtils.stringToPrimitiveCollection(argument, ",", inner);
					else {
						if (inner.equals(String.class))
							return Arrays.asList(argument.split(","));
						else return argument;
					}
				} else
					return argument;
			} catch (Exception e) {
				throw new InvalidArgumentException();
			}
		} else {
			ParameterParser parser = ReflectionUtils.constructOrNull(options.value());
			return parser.parse(parameter, argument);
		}
	}

}
