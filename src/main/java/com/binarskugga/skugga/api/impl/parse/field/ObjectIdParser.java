package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

public class ObjectIdParser implements FieldParser<ObjectId, String> {

	@Override
	public ObjectId parse(Field field, String value) throws CannotMapFieldException {
		return new ObjectId(value);
	}

	@Override
	public String unparse(Field field, ObjectId value) throws CannotMapFieldException {
		return value.toHexString();
	}

	@Override
	public boolean predicate(Field c) {
		return ObjectId.class.isAssignableFrom(c.getType());
	}

}
