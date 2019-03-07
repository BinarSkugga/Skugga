package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import com.eatthepath.uuid.FastUUID;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDParser implements FieldParser<UUID> {

	@Override
	public UUID parse(Field field, Object value) throws CannotMapFieldException {
		if(CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence cs = (CharSequence) value;
			return FastUUID.parseUUID(cs);
		}

		throw new CannotMapFieldException();
	}

	@Override
	public String unparse(Field field, UUID value) throws CannotMapFieldException {
		return FastUUID.toString(value);
	}

	@Override
	public boolean predicate(Field c) {
		return UUID.class.equals(c.getType());
	}

}
