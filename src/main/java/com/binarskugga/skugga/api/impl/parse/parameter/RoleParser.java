package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.Role;

import java.lang.reflect.Parameter;

public class RoleParser implements ParameterParser<Role, String> {

	@Override public Role parse(Parameter param, String s) throws Exception {
		return () -> s;
	}

	@Override public String unparse(Parameter context, Role object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Parameter c) {
		return Role.class.isAssignableFrom(c.getType());
	}

}
