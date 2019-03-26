package com.binarskugga.skugga.api.impl.endpoint;

import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.skugga.ServerProperties;
import com.binarskugga.skugga.api.AuthentifiableEntity;
import com.binarskugga.skugga.api.DataRepository;
import com.binarskugga.skugga.api.Token;
import com.binarskugga.skugga.api.annotation.Authenticator;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AuthController extends AbstractController {

	@Getter private DataRepository authRepository;
	@Getter private Class<? extends Token> tokenClass;

	@Override
	public AbstractController setSession(HttpSession session) {
		super.setSession(session);
		Reflections reflections = new Reflections(ServerProperties.getModelPackage());
		List<Class<? extends AuthentifiableEntity>> authenticatorClasses = reflections.getSubTypesOf(AuthentifiableEntity.class).stream()
				.filter(c -> Primitiva.Reflection.ofType(c).getAnnotation(Authenticator.class) != null)
				.collect(Collectors.toList());
		if (authenticatorClasses.size() > 0) {
			Class<? extends AuthentifiableEntity> authenticatorClass = authenticatorClasses.get(0);
			Class<? extends DataRepository> repositoryClass = authenticatorClass.getAnnotation(Authenticator.class).value();
			this.authRepository = Primitiva.Reflection.ofType(repositoryClass).create(authenticatorClass);
			this.tokenClass = ServerProperties.getTokenClass();
		}
		return this;
	}

}
