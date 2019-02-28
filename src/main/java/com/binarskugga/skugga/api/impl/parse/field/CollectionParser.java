package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionParser implements FieldParser<Collection, Collection> {

	@Override
	public Collection parse(Field field, Collection value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public Collection unparse(Field field, Collection value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public boolean predicate(Field c) {
		return Collection.class.isAssignableFrom(c.getType());
	}

}
