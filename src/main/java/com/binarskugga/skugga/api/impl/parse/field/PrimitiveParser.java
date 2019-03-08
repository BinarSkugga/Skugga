package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.primitiva.conversion.PrimitivaConversion;
import com.binarskugga.primitiva.conversion.PrimitivaConverter;
import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public class PrimitiveParser implements FieldParser<Object> {

	@Override
	@SuppressWarnings("unchecked")
	public Object parse(Field field, Object value) throws CannotMapFieldException {
		if (value.getClass().equals(field.getType()))
			return value;
		else if (CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence cs = (CharSequence) value;
			StringBuilder sb = new StringBuilder(cs.length()).append(cs);
			return PrimitivaConversion.single(String.class).convertTo(field.getType(), sb.toString());
		} else if (PrimitivaReflection.isPrimitiveOrBoxed(value.getClass())) {
			PrimitivaConverter<Object> primitiveConverter = PrimitivaConversion.single((Class<Object>) value.getClass());
			return primitiveConverter.convertTo(field.getType(), value);
		} else
			throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, Object value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public boolean predicate(Field c) {
		return PrimitivaReflection.isPrimitiveOrBoxed(c.getType());
	}

}
