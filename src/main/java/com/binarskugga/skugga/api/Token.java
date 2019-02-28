package com.binarskugga.skugga.api;

import com.binarskugga.skugga.util.CryptoUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public interface Token {

	default String generate() {
		return Jwts.builder()
				.setHeaderParam("typ", "JWS")
				.claim("ltt", Boolean.toString(this.isLTT()))
				.setSubject(this.getAuthentifier())
				.setIssuer(this.getIssuer())
				.setIssuedAt(new Date())
				.setExpiration(new Date(this.getExpires()))
				.signWith(CryptoUtils.getKey("token-sign.key"))
				.compact();
	}

	default void parse(String token) {
		Jws<Claims> data = Jwts.parser()
				.setAllowedClockSkewSeconds(120)
				.setSigningKey(CryptoUtils.getKey("token-sign.key"))
				.parseClaimsJws(token);

		this.setLTT(Boolean.parseBoolean((String) data.getBody().get("ltt")));
		this.setAuthentifier(data.getBody().getSubject());
		this.setExpires(data.getBody().getExpiration().getTime());
		this.setIssuer(data.getBody().getIssuer());
	}

	String getAuthentifier();

	void setAuthentifier(String authentifier);

	long getExpires();

	void setExpires(long expires);

	String getIssuer();

	void setIssuer(String issuer);

	boolean isLTT();

	void setLTT(boolean ltt);

	Role getRole();

	<AR extends Role> void setRole(AR role);

}
