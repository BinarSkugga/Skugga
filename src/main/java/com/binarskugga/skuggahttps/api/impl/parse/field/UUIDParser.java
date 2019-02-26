package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDParser implements FieldParser<UUID, String> {

	@Override
	public UUID parse(Field field, String value) throws CannotMapFieldException {
		return UUID.fromString(value);
	}

	@Override
	public String unparse(Field field, UUID value) throws CannotMapFieldException {
		return value.toString();
	}

	@Override
	public boolean predicate(Field c) {
		return UUID.class.equals(c.getType());
	}

}
