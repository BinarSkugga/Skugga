package com.binarskugga.skuggahttps.util;

import com.google.common.base.Charsets;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.ByteStreams;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class CryptoUtils {

	private static final String SALT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!/$%?&*()_-+=[]{}";
	private static final SecureRandom SRAND = new SecureRandom();
	private static Map<String, SecretKey> keyCache = new HashMap<>();
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private CryptoUtils() {}

	public static String salt(int length) {
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) {
			int index = SRAND.nextInt(SALT_CHARACTERS.length());
			sb.append(SALT_CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

	public static String hash(String raw, String salt) {
		SHA3.DigestSHA3 sha3512 = new SHA3.DigestSHA3(512);
		sha3512.reset();
		sha3512.update((raw + salt).getBytes(Charsets.UTF_8));

		byte[] hash = BCrypt.generate(sha3512.digest(), salt.getBytes(Charsets.UTF_8), 10);
		return Hex.toHexString(hash);
	}

	public static void createKeysIfNotExists(String... names) {
		boolean created = false;
		for(String name : names) {
			try {
				File file = ResourceUtils.getResourceFile(name);
				if (!file.exists() && file.createNewFile()) {
					created = true;
					SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
					try (FileWriter writer = new FileWriter(file)) {
						writer.write(new String(key.getEncoded(), Charsets.UTF_8));
					}
					logger.atInfo().log("Key '" + name + "' has been generated.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(created) {
			logger.atInfo().log("Signing keys were generated restart needed.");
			System.exit(0);
		}
	}

	public static SecretKey getKey(String name) {
		if(keyCache.containsKey(name)) return keyCache.get(name);
		else {
			SecretKey key = null;

			try {
				InputStream stream = ResourceUtils.getCompiledResource(name);
				key = Keys.hmacShaKeyFor(ByteStreams.toByteArray(stream));
				keyCache.put(name, key);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return key;
		}
	}


}
