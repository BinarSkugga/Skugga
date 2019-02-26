package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Field;

public class ClassParser implements FieldParser<Class, Object> {

	@Override
	public Class parse(Field field, Object value) throws CannotMapFieldException {
		if(value.getClass().equals(Class.class))
			return (Class) value;
		else if(CharSequence.class.isAssignableFrom(value.getClass()))
			return ReflectionUtils.forNameOrNull((String) value);
		else
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
