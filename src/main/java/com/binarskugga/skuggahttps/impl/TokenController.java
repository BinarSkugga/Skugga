package com.binarskugga.skuggahttps.impl;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.annotation.*;
import com.binarskugga.skuggahttps.api.exception.auth.*;
import com.binarskugga.skuggahttps.api.exception.entity.*;
import com.binarskugga.skuggahttps.api.impl.endpoint.*;
import com.binarskugga.skuggahttps.api.impl.map.*;
import com.binarskugga.skuggahttps.util.*;

import java.util.*;

@Controller("token")
public class TokenController extends AuthController {

	private static DefaultEndpointMapper mapper = new DefaultEndpointMapper();

	@Post
	public String ltt(Map<String, Object> loginMap) {
		Login login = (Login) mapper.toEntity(Arrays.asList(Login.class.getDeclaredFields()), loginMap);
		AuthentifiableEntity entity = (AuthentifiableEntity) this.getAuthRepository().load("authentifier", login.getAuthentifier());

		if(entity == null)
			throw new EntityInexistantException();
		if(!entity.validatePassword(login.getPassword()))
			throw new LoginException();

		Token token = ReflectionUtils.constructOrNull(this.getTokenClass());
		token.setStringAuthentifier(entity.getAuthentifier());
		token.setLTT(true);
		return token.generate();
	}

	@Post
	public String stt(String ltt) {
		Token token = ReflectionUtils.constructOrNull(this.getTokenClass());
		token.parse(ltt);
		AuthentifiableEntity entity = (AuthentifiableEntity) this.getAuthRepository().load("authentifier", token.getStringAuthentifier());
		if(entity == null)
			throw new EntityInexistantException();

		token.setStringAuthentifier(entity.getAuthentifier());
		token.setLTT(false);
		return token.generate();
	}

}
