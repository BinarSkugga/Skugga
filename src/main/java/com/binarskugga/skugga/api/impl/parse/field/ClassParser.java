package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class ClassParser implements FieldParser<Class> {

	@Override
	public Class parse(Field field, Object value) throws CannotMapFieldException {
		if (value.getClass().equals(Class.class))
			return (Class) value;
		else if (CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence cs = (CharSequence) value;
			StringBuilder sb = new StringBuilder(cs.length()).append(cs);
			return ClassTools.of(sb.toString()).get();
		} else
			throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, Class value) throws CannotMapFieldException {
		return value.getName();
	}

	@Override
	public boolean predicate(Field c) {
		return Class.class.equals(c.getType());
	}

}
