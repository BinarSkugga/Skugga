package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PrimitiveMapper implements FieldMapper {

	@Override
	public Object toMap(Field field, Object value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public Object toEntity(Field field, Object value) throws CannotMapFieldException {
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

}
