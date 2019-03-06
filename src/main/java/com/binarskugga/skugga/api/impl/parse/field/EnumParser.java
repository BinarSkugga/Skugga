package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;

import java.lang.reflect.Field;

public class EnumParser implements FieldParser<Enum, String> {

	@SuppressWarnings("unchecked")
	@Override public Enum parse(Field context, String object) throws Exception {
		Class<? extends Enum> enumClass = (Class<? extends Enum>) context.getType();
		return Enum.valueOf(enumClass, object);
	}

	@Override public String unparse(Field context, Enum object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Field c) {
		return Enum.class.isAssignableFrom(c.getType());
	}

}
