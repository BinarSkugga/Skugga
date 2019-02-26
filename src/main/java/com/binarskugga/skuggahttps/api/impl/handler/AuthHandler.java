package com.binarskugga.skuggahttps.api.impl.handler;

import com.binarskugga.skuggahttps.ServerProperties;
import com.binarskugga.skuggahttps.api.RequestHandler;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.annotation.Connected;
import com.binarskugga.skuggahttps.api.annotation.NotConnected;
import com.binarskugga.skuggahttps.api.exception.auth.InsufficientRoleException;
import com.binarskugga.skuggahttps.api.exception.auth.InvalidTokenException;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.api.impl.endpoint.HttpSession;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AuthHandler implements RequestHandler {

	@Override
	public boolean handle(HttpSession session) throws RuntimeException {
		Endpoint endpoint = session.getEndpoint();
		Class controller = endpoint.getAction().getDeclaringClass();
		if (ReflectionUtils.getMethodAnnotationOrNull(endpoint.getAction(), NotConnected.class) != null)
			return true;

		Connected connected = ReflectionUtils.getMethodAnnotationOrNull(endpoint.getAction(), Connected.class);
		if (connected == null) connected = ReflectionUtils.getClassAnnotationOrNull(controller, Connected.class);
		if (connected == null) return true;

		List<String> roles = Arrays.stream(connected.roles()).map(String::toUpperCase).collect(Collectors.toList());

		Token token = null;
		if (session.getRequestCookies().containsKey("token")) {
			try {
				Cookie cookie = session.getRequestCookies().get("token");
				token = this.createToken(cookie);
			} catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException e) {
				Cookie tokenCookie = new CookieImpl("token", "");
				tokenCookie.setExpires(new Date(new Date().getTime() - 1000));
				tokenCookie.setHttpOnly(true);
				tokenCookie.setPath("/");
				session.getExchange().setResponseCookie(tokenCookie);
				throw new InvalidTokenException();
			}
		}

		if (token == null)
			throw new InsufficientRoleException();

		if (token.isLTT())
			throw new InvalidTokenException();

		if (!roles.contains("*") && !roles.contains(token.getRole().name().toUpperCase()))
			throw new InsufficientRoleException();

		session.setToken(token);
		return true;
	}

	@SuppressWarnings("unchecked")
	private <T extends Token> T createToken(Cookie cookie) throws SignatureException, ExpiredJwtException, MalformedJwtException, UnsupportedJwtException {
		Token token = ReflectionUtils.constructOrNull(ServerProperties.getTokenClass());
		if (token == null) return null;

		token.parse(cookie.getValue());
		return (T) token;
	}

}
