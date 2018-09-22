package com.binarskugga.skuggahttps.auth;

import lombok.*;
import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.utils.*;
import com.binarskugga.skuggahttps.utils.security.cypher.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class AuthService {

	private static Logger logger = Logger.getLogger(AuthService.class.getName());
	private static AuthService instance = null;
	private SecretKey key;

	private AuthService() {
		File secretKeyFile = ResourceLoader.load("", "secret.key");
		PropertiesConfiguration configuration = HttpConfigProvider.get();
		if(!secretKeyFile.exists()) {
			int keyLenth = configuration.getInt("server.security.hmac.keylength").orElse(2048);
			if(keyLenth < 2048)
				logger.severe("HMAC key length is less than 2048 bit, insecure password hashes will be generated !");

			this.key = generateKey(configuration.getInt("server.security.hmac.keylength").orElse(2048));
			try(FileOutputStream os = new FileOutputStream(secretKeyFile)) {
				os.write(key.getEncoded());
			} catch(Exception e) { e.printStackTrace(); }
		} else {
			try {
				byte[] encoded = Files.readAllBytes(Paths.get(secretKeyFile.toURI()));
				this.key = new SecretKeySpec(encoded, "HMACSHA512");

				int configKeySize = configuration.getInt("server.security.hmac.keylength").orElse(2048);
				if((encoded.length * 8) != configKeySize)
					logger.severe("Outdated HMAC key length ! Please clean your target to update your secret to the new size."
							+ "(Current=" + (encoded.length * 8) + ", Config=" + configKeySize);
			} catch(Exception e) { e.printStackTrace(); }
		}
	}

	@Synchronized
	public static AuthService get() {
		if(instance == null) {
			instance = new AuthService();
		}
		return instance;
	}

	public SecretKey generateKey(int length) {
		SecretKey key = null;
		try {
			KeyGenerator generator = KeyGenerator.getInstance("HMACSHA512");
			generator.init(length);
			key = generator.generateKey();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return key;
	}

	public String hashPassword(String password) {
		return HSha512.hash(this.key.getEncoded(), password);
	}

	public Token createToken(GenericUser user) {
		long now = new Date().getTime();
		long expires = now + TimeUnit.DAYS.toSeconds(14);
		String token = HSha512.hash(this.key.getEncoded(), user.getPasswordHash() + now);
		return new Token(expires, token, user.getId().toString());
	}

	public boolean isTokenValid(GenericUser user, Token token) {
		long generatedTime = token.getExpires() - TimeUnit.DAYS.toSeconds(14);
		String reconstructed = HSha512.hash(this.key.getEncoded(), user.getPasswordHash() + generatedTime);
		return token.getTokenHash().equals(reconstructed) && token.getId().equalsIgnoreCase(user.getId().toString());
	}
}
