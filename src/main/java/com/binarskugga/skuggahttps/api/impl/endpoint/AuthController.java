package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.ServerProperties;
import com.binarskugga.skuggahttps.api.AuthentifiableEntity;
import com.binarskugga.skuggahttps.api.DataRepository;
import com.binarskugga.skuggahttps.api.Token;
import com.binarskugga.skuggahttps.api.annotation.Authenticator;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
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
				.filter(c -> ReflectionUtils.getClassAnnotationOrNull(c, Authenticator.class) != null)
				.collect(Collectors.toList());
		if (authenticatorClasses.size() > 0) {
			Class<? extends AuthentifiableEntity> authenticatorClass = authenticatorClasses.get(0);
			Class<? extends DataRepository> repositoryClass = authenticatorClass.getAnnotation(Authenticator.class).value();
			this.authRepository = ReflectionUtils.constructOrNull(repositoryClass, authenticatorClass);
			this.tokenClass = ServerProperties.getTokenClass();
		}
		return this;
	}

}
