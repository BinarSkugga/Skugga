package com.binarskugga.skugga.api;

import com.binarskugga.skugga.util.CryptoUtils;

import java.io.Serializable;
import java.util.Date;

public interface AuthentifiableEntity<E extends AuthentifiableEntity<E, I>, I extends Serializable> extends BaseEntity<I, E> {

	String getAuthentifier();

	E setAuthentifier(String authentifier);

	Role getRole();

	E setRole(Role role);

	default String getRoleName() {
		return this.getRole().name();
	}

	String getPasswordHash();

	E setPasswordHash(String paswordHash);

	String getPasswordSalt();

	E setPasswordSalt(String salt);

	long getLastPasswordChange();

	E setLastPasswordChange(long newDate);

	@SuppressWarnings("unchecked")
	default E updateLastPasswordChange() {
		this.setLastPasswordChange(new Date().getTime() - 1000);
		return (E) this;
	}

	default boolean validatePassword(String otherPass) {
		String hc = CryptoUtils.hash(otherPass, this.getPasswordSalt());
		return this.getPasswordHash().equals(hc);
	}

	@SuppressWarnings("unchecked")
	default E changePassword(String newPassword) {
		this.setPasswordSalt(CryptoUtils.salt(16));
		this.setPasswordHash(CryptoUtils.hash(newPassword, this.getPasswordSalt()));
		this.updateLastPasswordChange();
		return (E) this;
	}

}
