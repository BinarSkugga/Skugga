package com.binarskugga.skuggahttps.utils;

import com.sun.net.httpserver.*;
import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.http.*;

import java.util.*;

public class HttpUtils {

	public static Cookie getCookie(String name, Headers headers) {
		for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
			if(entry.getKey().toLowerCase().contentEquals("cookie")) {
				String[] cookies = entry.getValue().get(0).split(";");
				for(String cookie : cookies) {
					if(cookie.trim().startsWith(name)) {
						return Cookie.fromHeader(cookie.trim());
					}
				}
			}
		}
		return null;
	}

	public static boolean validateToken(GenericUser user, Token token) {
		if(user == null) {
			return false;
		}
		if(token.getExpires() <= new Date().getTime()) {
			return false;
		}
		return AuthService.get().isTokenValid(user, token);
	}
}
