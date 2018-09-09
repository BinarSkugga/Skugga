package com.binarskugga.skuggahttps.controller;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.auth.role.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.utils.security.*;
import org.shredzone.acme4j.util.*;

import java.security.*;

@Controller("/.well-known")
public class SecurityController extends AbstractController {

	private static ACMEClient client;
	private static String token;
	private static String content;

	@Get
	@ContentType("text/plain")
	@Access({ AdminRole.class })
	public String initialize() {
		client = new ACMEClient(true, "test@binarskugga.com") {
			@Override
			public void processHttpChallenge(String token, String content) {
				SecurityController.token = token;
				SecurityController.content = content;
			}
		};

		return "Initialized.";
	}

	@Get("order/{string}")
	@ContentType("text/plain")
	@Access({ AdminRole.class })
	public String order(String domain) {
		if(client.order(domain)) {
			try {
				KeyPair domainKeys = KeyPairUtils.createKeyPair(2048);
				CSRBuilder csr = new CSRBuilder();
				csr.addDomain(domain);
				csr.setOrganization("Boi");
				csr.sign(domainKeys);
			} catch(Exception e) {
				return "Failed to create CSR.";
			}
			return "SSL certificate ordered";
		} else
			return "Failed to order.";
	}

	@Get("acme-challenge/{string}")
	@ContentType("text/plain")
	@Access({ AdminRole.class })
	public String challenge(String token) {
		if(token.equalsIgnoreCase(SecurityController.token))
			return SecurityController.content;
		return "Bad token !";
	}

}
