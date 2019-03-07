package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class EnumParser implements FieldParser<Enum> {

	@SuppressWarnings("unchecked")
	@Override
	public Enum parse(Field context, Object object) throws Exception {
		if(CharSequence.class.isAssignableFrom(object.getClass())) {
			CharSequence cs = (CharSequence) object;
			StringBuilder sb = new StringBuilder(cs.length()).append(cs);
			Class<? extends Enum> enumClass = (Class<? extends Enum>) context.getType();
			return Enum.valueOf(enumClass, sb.toString());
		}

		throw new CannotMapFieldException();
	}

	@Override public Object unparse(Field context, Enum object) throws Exception {
		return object.name();
	}

	@Override public boolean predicate(Field c) {
		return Enum.class.isAssignableFrom(c.getType());
	}

}
