package com.binarskugga.skuggahttps.api.impl;

import com.binarskugga.skuggahttps.api.Role;
import com.binarskugga.skuggahttps.api.Token;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public abstract class BasicToken implements Token {

	@Accessors(chain = false)
	@Getter
	@Setter
	private String authentifier;

	@Accessors(chain = false)
	@Getter
	@Setter
	private long expires;

	@Accessors(chain = false)
	@Getter
	@Setter
	private String issuer;

	@Accessors(chain = false)
	@Getter
	@Setter
	private boolean LTT;

	@Accessors(chain = false)
	@Getter
	private Role role;

	@Override
	public <AR extends Role> void setRole(AR role) {
		this.role = role;
	}

}
