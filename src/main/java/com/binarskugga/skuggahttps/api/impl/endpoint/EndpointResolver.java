package com.binarskugga.skuggahttps.api.impl.endpoint;

import com.binarskugga.skuggahttps.api.annotation.ContentType;
import com.binarskugga.skuggahttps.api.annotation.Controller;
import com.binarskugga.skuggahttps.api.annotation.Get;
import com.binarskugga.skuggahttps.api.annotation.Post;
import com.binarskugga.skuggahttps.api.enums.HttpMethod;
import com.binarskugga.skuggahttps.util.EndpointUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.flogger.FluentLogger;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.Getter;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class EndpointResolver {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();
	@Getter private List<Endpoint> endpoints;
	@Getter private List<Endpoint> socketCallbacks;

	private final Cache<String, Endpoint> routingCache;
	private final BloomFilter<CharSequence> routingCacheFilter;

	@SuppressWarnings("unchecked")
	public EndpointResolver(String controllerPackage, String root) {
		this.endpoints = new ArrayList<>();
		this.socketCallbacks = new ArrayList<>();

		Reflections reflections = new Reflections(controllerPackage);
		Set<Class<? extends AbstractController>> controllers = reflections.getTypesAnnotatedWith(Controller.class).stream()
				.filter(AbstractController.class::isAssignableFrom).map(c -> (Class<? extends AbstractController>)c)
				.collect(Collectors.toSet());

		for(Class<? extends AbstractController> controller : controllers) {
			this.registerController(root, controller);
		}

		this.routingCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.HOURS).build();
		this.routingCacheFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 1000);
	}

	public Endpoint getEndpoint(String search, HttpMethod method) {
		String sanitizedSearch = EndpointUtils.sanitizePath(search);
		Endpoint found = null;
		if(this.routingCacheFilter.mightContain(sanitizedSearch)) {
			found = this.routingCache.getIfPresent(sanitizedSearch);
			if(found != null && found.getMethod() != method && method != HttpMethod.OPTIONS) found = null;
		}
		if(found == null) {
			for(Endpoint entry : this.endpoints) {
				if(entry.getMethod() != method && method != HttpMethod.OPTIONS) continue;
				if(this.matchEndpoint(entry.getRoute(), sanitizedSearch)) {
					this.routingCacheFilter.put(sanitizedSearch);
					this.routingCache.put(sanitizedSearch, entry);
					found = entry;
					break;
				}
			}
		}
		return found;
	}

	public boolean matchEndpoint(String endpoint, String request) {
		String[] brokenEndpoint = endpoint.split("/");
		String[] brokenRequest = request.split("/");

		if(brokenEndpoint.length != brokenRequest.length) {
			return false;
		}

		for(int i = 0; i < brokenEndpoint.length; i++) {
			String part = brokenEndpoint[i];
			if(!part.equals("$"))
				if(!part.equals(brokenRequest[i]))
					return false;
		}
		return true;
	}

	public void registerController(String root, Class<? extends AbstractController> controller) {
		String controllerPath = controller.getAnnotation(Controller.class).value();
		if(controllerPath.equals(""))
			controllerPath = root + "/" + controller.getSimpleName().toLowerCase();
		else if(controllerPath.equals("."))
			controllerPath = "";
		else
			controllerPath = root + "/" + controllerPath;

		Set<Method> methods = ReflectionUtils.getMethods(controller, (Predicate<Method>) method -> {
			method.setAccessible(true);
			return (method.isAnnotationPresent(Get.class) || method.isAnnotationPresent(Post.class))
					&& !Modifier.isVolatile(method.getModifiers());
		});

		for(Method method : methods) {
			method.setAccessible(true);
			if (!method.isAnnotationPresent(Get.class) && !method.isAnnotationPresent(Post.class))
				continue;

			Endpoint endpoint = new Endpoint();
			String methodPath;
			if (method.isAnnotationPresent(Get.class)) {
				methodPath =
						controllerPath + "/" + (method.getAnnotation(Get.class).value().equals("")
								? method.getName().replaceAll("_", "/")
								: method.getAnnotation(Get.class).value());
				endpoint.setMethod(HttpMethod.GET);
				endpoint.setReturnType(method.getGenericReturnType());
			} else {
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

			if(method.isAnnotationPresent(ContentType.class)) {
				ContentType contentType = method.getAnnotation(ContentType.class);
				endpoint.setContentType(contentType.value());
			}

			endpoint.setAction(method);
			endpoint.setRoute(EndpointUtils.sanitizePath(methodPath));
			this.endpoints.add(endpoint);
		}
	}

}
