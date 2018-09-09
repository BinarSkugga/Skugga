package com.binarskugga.skuggahttps.http.path;

import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.auth.role.*;
import lombok.*;

import java.lang.reflect.*;
import java.util.*;

public class Endpoint {

	@Getter @Setter EndpointType type;
	@Getter @Setter Method action;
	@Getter @Setter String route;
	@Getter @Setter String fullRoute;
	@Getter @Setter List<Class<? extends AccessRole>> access;
	@Getter @Setter Parameter subject;

}
