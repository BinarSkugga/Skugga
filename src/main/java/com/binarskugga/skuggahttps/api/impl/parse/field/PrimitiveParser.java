package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PrimitiveParser implements FieldParser<Object, Object> {

	@Override
	public Object parse(Field field, Object value) throws CannotMapFieldException {
		if(value.getClass().equals(field.getType()))
			return value;
		else if(CharSequence.class.isAssignableFrom(value.getClass())) {
			CharSequence str = (CharSequence) value;
			return ReflectionUtils.stringToPrimitive(str.toString(), field.getType());
		} else if(Number.class.isAssignableFrom(value.getClass())) {
			Number number = (Number) value;
			if(ReflectionUtils.isNumericPrimitiveOrBoxed(field.getType()))
				return value;
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(field.getType(), Boolean.class, boolean.class))
				return number.intValue() == 1;
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(field.getType(), Character.class, char.class))
				return (char) (number.intValue() + '0');
		} else if(ReflectionUtils.typeEqualsIgnoreBoxing(value.getClass(), field.getType(), Boolean.class, boolean.class)) {
			return value;
		} else if(ReflectionUtils.typeEqualsIgnoreBoxing(value.getClass(), field.getType(), Character.class, char.class)) {
			return value;
		}

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
