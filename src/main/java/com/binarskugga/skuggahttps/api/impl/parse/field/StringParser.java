package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class StringParser implements FieldParser<CharSequence, Object> {

	@Override
	public CharSequence parse(Field field, Object value) throws CannotMapFieldException {
		return value.toString();
	}

	@Override
	public Object unparse(Field field, CharSequence value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public boolean predicate(Field c) {
		return CharSequence.class.isAssignableFrom(c.getType());
	}

}
