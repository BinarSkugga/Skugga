package com.binarskugga.skuggahttps.api.impl.handler;

import com.binarskugga.skuggahttps.api.RequestHandler;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.annotation.Connected;
import com.binarskugga.skuggahttps.api.annotation.NotConnected;
import com.binarskugga.skuggahttps.api.exception.auth.InsufficientRoleException;
import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthHandler implements RequestHandler {

	@Override
	public boolean handle(HttpSession session) throws RuntimeException {
		Endpoint endpoint = session.getEndpoint();
		Class controller = endpoint.getAction().getDeclaringClass();
		if(ReflectionUtils.getMethodAnnotationOrNull(endpoint.getAction(), NotConnected.class) != null)
			return true;

		Connected connected = ReflectionUtils.getMethodAnnotationOrNull(endpoint.getAction(), Connected.class);
		if(connected == null) connected = ReflectionUtils.getClassAnnotationOrNull(controller, Connected.class);
		if(connected == null) return true;

		List<String> roles = Arrays.stream(connected.roles()).map(String::toUpperCase).collect(Collectors.toList());
		Token token = session.getToken();
		if(token == null)
			throw new InsufficientRoleException();

		if(!roles.contains("*") && !roles.contains(token.getRole().name().toUpperCase()))
			throw new InsufficientRoleException();

		return true;
	}

}
