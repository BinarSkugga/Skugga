package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.api.annotation.ContentType;
import com.binarskugga.skuggahttps.api.annotation.Controller;
import com.binarskugga.skuggahttps.api.annotation.Get;
import com.binarskugga.skuggahttps.api.annotation.Post;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.impl.TokenController;
import com.binarskugga.skuggahttps.util.EndpointUtils;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import com.google.common.flogger.FluentLogger;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EndpointResolver {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	@Getter private List<Endpoint> endpoints;
	@Getter private List<Endpoint> socketCallbacks;

	@SuppressWarnings("unchecked")
	public EndpointResolver(String controllerPackage, String root) {
		this.endpoints = new ArrayList<>();
		this.socketCallbacks = new ArrayList<>();

		Reflections reflections = new Reflections(controllerPackage);
		Set<Class<? extends AbstractController>> controllers = reflections.getTypesAnnotatedWith(Controller.class).stream()
				.filter(AbstractController.class::isAssignableFrom).map(c -> (Class<? extends AbstractController>) c)
				.collect(Collectors.toSet());

		this.registerController(root, TokenController.class);
		for (Class<? extends AbstractController> controller : controllers) {
			this.registerController(root, controller);
		}
	}

	public Endpoint getEndpoint(String search, HttpMethod method) {
		String sanitizedSearch = EndpointUtils.sanitizePath(search);
		for (Endpoint entry : this.endpoints) {
			if (entry.getMethod() != method && method != HttpMethod.OPTIONS) continue;
			if (this.matchEndpoint(entry.getRoute(), sanitizedSearch)) {
				return entry;
			}
		}
		return null;
	}

	public boolean matchEndpoint(String endpoint, String request) {
		String[] brokenEndpoint = endpoint.split("/");
		String[] brokenRequest = request.split("/");

		if (brokenEndpoint.length != brokenRequest.length) {
			return false;
		}

		for (int i = 0; i < brokenEndpoint.length; i++) {
			String part = brokenEndpoint[i];
			if (!part.equals("$"))
				if (!part.equals(brokenRequest[i]))
					return false;
		}
		return true;
	}

	public void registerController(String root, Class<? extends AbstractController> controller) {
		String controllerPath = EndpointUtils.getControllerPath(root, controller);

		List<Method> methods = ReflectionUtils.getAllMethods(controller).stream().filter(m ->
				(m.isAnnotationPresent(Get.class) || m.isAnnotationPresent(Post.class))
						&& !Modifier.isVolatile(m.getModifiers())
		).collect(Collectors.toList());

		for (Method method : methods) {
			method.setAccessible(true);

			Endpoint endpoint = new Endpoint();
			String methodPath = "";
			if (method.isAnnotationPresent(Get.class)) {
				methodPath =
						controllerPath + "/" + (method.getAnnotation(Get.class).value().equals("")
								? method.getName().replaceAll("_", "/")
								: method.getAnnotation(Get.class).value());
				endpoint.setMethod(HttpMethod.GET);
				endpoint.setReturnType(method.getGenericReturnType());
			} else if (method.isAnnotationPresent(Post.class)) {
				methodPath =
						controllerPath + "/" + (method.getAnnotation(Post.class).value().equals("")
								? method.getName().replaceAll("_", "/")
								: method.getAnnotation(Post.class).value());
				endpoint.setMethod(HttpMethod.POST);
				endpoint.setReturnType(method.getGenericReturnType());

				if (method.getGenericParameterTypes().length > 0)
					endpoint.setBodyType(method.getGenericParameterTypes()[0]);
				else
					logger.atWarning().log("POST endpoint without body ! (" + methodPath + ")");
			}

			if (method.isAnnotationPresent(ContentType.class)) {
				ContentType contentType = method.getAnnotation(ContentType.class);
				endpoint.setContentType(contentType.value());
			}

			endpoint.setAction(method);
			endpoint.setRoute(EndpointUtils.sanitizePath(methodPath));

			this.endpoints.add(endpoint);
		}
	}

}
