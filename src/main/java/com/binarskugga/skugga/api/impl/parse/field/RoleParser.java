package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.Role;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class RoleParser implements FieldParser<Role> {

	@Override
	public Role parse(Field field, Object object) throws Exception {
		if(CharSequence.class.isAssignableFrom(object.getClass())) {
			CharSequence cs = (CharSequence) object;
			StringBuilder sb = new StringBuilder(cs.length()).append(cs);
			return sb::toString;
		}

		throw new CannotMapFieldException();
	}

	@Override public Object unparse(Field context, Role object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Field c) {
		return Role.class.isAssignableFrom(c.getType());
	}

}
