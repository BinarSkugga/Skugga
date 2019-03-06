package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.Role;

import java.lang.reflect.Field;

public class RoleParser implements FieldParser<Role, String> {

	@Override public Role parse(Field field, String s) throws Exception {
		return () -> s;
	}

	@Override public String unparse(Field context, Role object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Field c) {
		return Role.class.isAssignableFrom(c.getType());
	}

}
