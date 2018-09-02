package com.binarskugga.skuggahttps.utils.security;

import com.binarskugga.skuggahttps.utils.*;
import org.shredzone.acme4j.*;
import org.shredzone.acme4j.challenge.*;
import org.shredzone.acme4j.util.*;

import java.io.*;
import java.security.*;

public abstract class ACMEClient {

	public Session session;
	public Login account;

	public Metadata metadata;

	public ACMEClient(boolean staging, String account) {
		System.out.println("Proceeding to order new certificate !");
		try {
			if(staging) {
				this.session = new Session("acme://letsencrypt.org/staging");
			} else {
				this.session = new Session("acme://letsencrypt.org");
			}
			this.metadata = this.session.getMetadata();
		} catch(Exception e) {
			System.err.println("Can't open ACME session !");
		}

		if(!this.isRegistered()) {
			// REGISTER
			KeyPair accountKeys = KeyPairUtils.createKeyPair(2048);
			try {
				this.account = new AccountBuilder().addContact("mailto:" + account).agreeToTermsOfService().useKeyPair(accountKeys).createLogin(this.session);
			} catch(Exception e) {
				System.err.println("Couldn't register account !");
				System.err.println("This might be due to new Terms of Service please visit:" + this.metadata.getTermsOfService().toString());
				return;
			}
			try(FileWriter fw = new FileWriter("letsencrypt-account.pem")) {
				KeyPairUtils.writeKeyPair(accountKeys, fw);
			} catch(Exception e) {
				System.err.println("KeyPair file cannot be created !");
			}
		}

		System.out.println("LoggedAccess in as: " + this.account.getAccountLocation());
	}

	public void order(String... domains) {
		try {
			Order order = this.account.getAccount().newOrder().domains(domains).create();
			for(Authorization auth : order.getAuthorizations()) {
				if(auth.getStatus() != Status.VALID) {
					processAuth(auth);
				}
			}
		} catch(Exception e) {
			System.err.println("Couldn't create a new certificate order !");
		}
	}

	protected abstract void createHTTPResource(String token, String content);

	private void processAuth(Authorization auth) {
		System.out.println("Challenging domain '" + auth.getDomain() + "'...");
		try {
			Http01Challenge httpChallenge = auth.findChallenge(Http01Challenge.TYPE);
			this.createHTTPResource(httpChallenge.getToken(), httpChallenge.getAuthorization());

			httpChallenge.trigger();
			while(auth.getStatus() != Status.VALID && auth.getStatus() != Status.INVALID) {
				Thread.sleep(500);
				auth.update();
			}
			if(auth.getStatus() == Status.INVALID) {
				System.err.println("Failed to order new certificate !");
			}
		} catch(Exception e) {
			System.err.println("Couldn't execute the DNS challenge !");
		}
	}

	private boolean isRegistered() {
		File file = ResourceLoader.load("", "letsencrypt-account.pem");
		if(file.exists()) {
			KeyPair accountKeys = null;
			try(FileReader fr = new FileReader(file)) {
				accountKeys = KeyPairUtils.readKeyPair(fr);
			} catch(Exception e) {
				System.err.println("KeyPair file is corrupted !");
				return false;
			}

			try {
				this.account = new AccountBuilder().onlyExisting().useKeyPair(accountKeys).createLogin(this.session);
			} catch(Exception e) {
				return false;
			}

			return true;
		}
		return false;
	}
}
