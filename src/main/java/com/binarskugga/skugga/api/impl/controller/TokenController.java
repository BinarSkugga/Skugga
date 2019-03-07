package com.binarskugga.skugga.api.impl.controller;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.AuthentifiableEntity;
import com.binarskugga.skugga.api.Token;
import com.binarskugga.skugga.api.annotation.Controller;
import com.binarskugga.skugga.api.annotation.Post;
import com.binarskugga.skugga.api.exception.InvalidArgumentException;
import com.binarskugga.skugga.api.exception.auth.InvalidTokenException;
import com.binarskugga.skugga.api.exception.auth.LoginException;
import com.binarskugga.skugga.api.exception.auth.NoAuthException;
import com.binarskugga.skugga.api.exception.entity.EntityInexistantException;
import com.binarskugga.skugga.api.impl.endpoint.AuthController;

@Controller("token")
public class TokenController extends AuthController {

	@SuppressWarnings("unchecked")
	@Post
	public String ltt(Login login) {
		if(this.getAuthRepository() == null)
			throw new NoAuthException();

		if (login == null)
			throw new InvalidArgumentException();

		AuthentifiableEntity entity = (AuthentifiableEntity) this.getAuthRepository().load("authentifier", login.getAuthentifier());

		if (entity == null)
			throw new EntityInexistantException();
		if (!entity.validatePassword(login.getPassword()))
			throw new LoginException();

		Token token = PrimitivaReflection.constructOrNull(this.getTokenClass());
		token.setAuthentifier(entity.getAuthentifier());
		token.setLTT(true);
		return token.generate();
	}

	@SuppressWarnings("unchecked")
	@Post
	public String stt(String ltt) {
		if(this.getAuthRepository() == null)
			throw new NoAuthException();

		Token token = PrimitivaReflection.constructOrNull(this.getTokenClass());
		if (token == null)
			throw new InvalidTokenException();

		token.parse(ltt);
		if (!token.isLTT())
			throw new InvalidTokenException();

		AuthentifiableEntity entity = (AuthentifiableEntity) this.getAuthRepository().load("authentifier", token.getAuthentifier());
		if (entity == null)
			throw new EntityInexistantException();

		token.setAuthentifier(entity.getAuthentifier());
		token.setLTT(false);
		return token.generate();
	}

}
