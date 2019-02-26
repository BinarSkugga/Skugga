package com.binarskugga.skuggahttps.impl;

import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor
public class Login {

	@Getter @Setter private String authentifier;
	@Getter @Setter private String password;

}
