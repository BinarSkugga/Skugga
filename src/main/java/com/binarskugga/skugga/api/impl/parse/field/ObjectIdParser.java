package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

public class ObjectIdParser implements FieldParser<ObjectId> {

	@Override
	public ObjectId parse(Field field, Object value) throws CannotMapFieldException {
		if(CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence cs = (CharSequence) value;
			StringBuilder sb = new StringBuilder(cs.length()).append(cs);
			return new ObjectId(sb.toString());
		}

		throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, ObjectId value) throws CannotMapFieldException {
		return value.toHexString();
	}

	@Override
	public boolean predicate(Field c) {
		return ObjectId.class.isAssignableFrom(c.getType());
	}

}
