package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Field;

public class ClassMapper implements FieldMapper<Object, Class> {

	@Override
	public Object toMap(Field field, Class value) throws CannotMapFieldException {
		return value.getName();
	}

	@Override
	public Class toEntity(Field field, Object value) throws CannotMapFieldException {
		if(value.getClass().equals(Class.class))
			return (Class) value;
		else if(value.getClass().equals(String.class))
			return ReflectionUtils.forNameOrNull((String) value);
		else
			throw new CannotMapFieldException();
	}

}
