package com.binarskugga.skuggahttps.api.impl.token;

import com.binarskugga.skuggahttps.api.Role;
import com.binarskugga.skuggahttps.api.Token;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

public abstract class BasicToken<I extends Serializable> implements Token<I> {

	@Accessors(chain = false)
	@Getter @Setter private I authentifier;

	@Accessors(chain = false)
	@Getter @Setter private long issuedAt;

	@Accessors(chain = false)
	@Getter @Setter private long expires;

	@Accessors(chain = false)
	@Getter @Setter private String issuer;

	@Accessors(chain = false)
	@Getter private Role role;

	@Override
	public <AR extends Role> void setRole(AR role) {
		this.role = role;
	}

}