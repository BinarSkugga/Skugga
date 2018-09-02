package com.binarskugga.skuggahttps.http.path;

import lombok.*;

import java.lang.reflect.*;

public class Endpoint {

	@Getter @Setter EndpointType type;
	@Getter @Setter Method action;
	@Getter @Setter String route;
	@Getter @Setter String fullRoute;

}
