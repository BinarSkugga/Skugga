package com.binarskugga.skuggahttps.util;

import com.binarskugga.skuggahttps.api.annotation.Controller;
import com.binarskugga.skuggahttps.api.impl.endpoint.AbstractController;

public class EndpointUtils {

	private EndpointUtils() {
	}

	public static String sanitizePath(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}

	public static String getControllerPath(String root, Class<? extends AbstractController> controller) {
		String controllerPath = controller.getAnnotation(Controller.class).value();
		if (controllerPath.equals(""))
			return root + "/" + controller.getSimpleName().toLowerCase();
		else if (controllerPath.equals("."))
			return "";
		else
			return root + "/" + controllerPath;
	}

}
