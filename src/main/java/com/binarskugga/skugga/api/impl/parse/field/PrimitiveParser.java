package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import com.binarskugga.skugga.util.ReflectionUtils;
import com.binarskugga.skugga.util.conversion.PrimitiveConversionUtils;

import java.lang.reflect.Field;

public class PrimitiveParser implements FieldParser<Object, Object> {

	@Override
	public Object parse(Field field, Object value) throws CannotMapFieldException {
		if (value.getClass().equals(field.getType()))
			return value;
		else if (CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence str = (CharSequence) value;
			return ReflectionUtils.stringToPrimitive(str.toString(), field.getType());
		} else if (ReflectionUtils.isPrimitiveOrBoxed(value.getClass()))
			return PrimitiveConversionUtils.single(value.getClass()).convertTo(field.getType(), value);
		else
			throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, Object value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public boolean predicate(Field c) {
		return ReflectionUtils.isPrimitiveOrBoxed(c.getType());
	}

}
