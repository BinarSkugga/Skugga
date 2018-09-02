package com.binarskugga.skuggahttps.http;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cookie {

	@Getter @Setter public String name;

	@Getter @Setter public String value;

	@Builder.Default
	@Getter @Setter public String path = "/";

	public static Cookie fromHeader(String header) {
		String[] brokenData = header.split("=");

		Cookie cookie = new Cookie();
		cookie.setName(brokenData[0].trim());
		cookie.setValue(brokenData[1].trim());

		return cookie;
	}

	@Override
	public String toString() {
		return this.name + "=" + this.value + "; Path=" + this.path + "; HttpOnly=true; Secure=true";
	}
}
