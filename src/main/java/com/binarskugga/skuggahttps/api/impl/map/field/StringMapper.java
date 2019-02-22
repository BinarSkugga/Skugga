package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class StringMapper implements FieldMapper<Object, CharSequence> {

	@Override
	public Object toMap(Field field, CharSequence value) throws CannotMapFieldException {
		return value.toString();
	}

	@Override
	public CharSequence toEntity(Field field, Object value) throws CannotMapFieldException {
		return ((CharSequence) value).toString();
	}

}
