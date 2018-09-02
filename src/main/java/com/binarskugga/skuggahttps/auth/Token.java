package com.binarskugga.skuggahttps.auth;

import lombok.*;
import com.binarskugga.skuggahttps.http.api.exception.*;
import com.binarskugga.skuggahttps.utils.security.cypher.*;

@AllArgsConstructor
public class Token {

	@Getter @Setter private long expires;

	@Getter @Setter private String tokenHash;

	@Getter @Setter private String id;

	public Token(String token) {
		try {
			String decoded = Base64.decode(token);
			String[] parts = decoded.split(":");
			this.expires = Long.parseLong(parts[0]);
			this.id = parts[1];
			this.tokenHash = parts[2];
		} catch(Exception e) {
			throw new InvalidTokenException();
		}
	}

	public String getFullToken() {
		return Base64.encode(this.expires + ":" + this.id + ":" + this.tokenHash);
	}
}
