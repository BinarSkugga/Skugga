package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class StringArrayMapper implements FieldMapper<Object, CharSequence[]> {

	@Override
	public Object toMap(Field field, CharSequence[] value) throws CannotMapFieldException {
		return Arrays.asList(value);
	}

	@Override
	public CharSequence[] toEntity(Field field, Object value) throws CannotMapFieldException {
		if(value.getClass().equals(CharSequence[].class))
			return (CharSequence[]) value;
		else if(Collection.class.isAssignableFrom(value.getClass()))
			return (CharSequence[]) ((Collection) value).stream().map(Object::toString).toArray(String[]::new);
		else if(CharSequence.class.isAssignableFrom(value.getClass()))
			return ((CharSequence) value).toString().split(",");
		else
			throw new CannotMapFieldException();
	}

}
