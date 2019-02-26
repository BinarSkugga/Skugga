package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class StringArrayParser implements FieldParser<CharSequence[], Object> {

	@Override
	@SuppressWarnings("unchecked")
	public CharSequence[] parse(Field field, Object value) throws CannotMapFieldException {
		if (value.getClass().equals(CharSequence[].class))
			return (CharSequence[]) value;
		else if (Collection.class.isAssignableFrom(value.getClass()))
			return (CharSequence[]) ((Collection) value).stream().map(Object::toString).toArray(String[]::new);
		else if (CharSequence.class.isAssignableFrom(value.getClass()))
			return ((CharSequence) value).toString().split(",");
		else
			throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, CharSequence[] value) throws CannotMapFieldException {
		return Arrays.asList(value);
	}

	@Override
	public boolean predicate(Field c) {
		return CharSequence[].class.equals(c.getType()) || String[].class.equals(c.getType());
	}

}
