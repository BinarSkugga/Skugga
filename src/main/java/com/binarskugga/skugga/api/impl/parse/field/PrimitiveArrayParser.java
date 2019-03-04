package com.binarskugga.skugga.api.impl.parse.field;

import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import com.binarskugga.skugga.util.ReflectionUtils;
import com.binarskugga.skugga.util.conversion.PrimitiveConversionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class PrimitiveArrayParser implements FieldParser<Object, Object> {

	@Override
	@SuppressWarnings("unchecked")
	public Object parse(Field field, Object value) throws CannotMapFieldException {
		if (Collection.class.isAssignableFrom(value.getClass())) {
			Object array = ReflectionUtils.primitiveCollectionToArray((Collection) value);
			return PrimitiveConversionUtils.of(array.getClass()).convertTo(field.getType(), array);
		}

		throw new CannotMapFieldException();
	}

	@Override
	public Object unparse(Field field, Object value) throws CannotMapFieldException {
		if (value.getClass().equals(byte[].class))
			return Arrays.asList(ArrayUtils.toObject((byte[]) value));
		else if (value.getClass().equals(short[].class))
			return Arrays.asList(ArrayUtils.toObject((short[]) value));
		else if (value.getClass().equals(int[].class))
			return Arrays.asList(ArrayUtils.toObject((int[]) value));
		else if (value.getClass().equals(long[].class))
			return Arrays.asList(ArrayUtils.toObject((long[]) value));
		else if (value.getClass().equals(float[].class))
			return Arrays.asList(ArrayUtils.toObject((float[]) value));
		else if (value.getClass().equals(double[].class))
			return Arrays.asList(ArrayUtils.toObject((double[]) value));
		else if (value.getClass().equals(boolean[].class))
			return Arrays.asList(ArrayUtils.toObject((boolean[]) value));
		else if (value.getClass().equals(char[].class))
			return Arrays.asList(ArrayUtils.toObject((char[]) value));
		else if (value.getClass().equals(Byte[].class))
			return Arrays.asList((Byte[]) value);
		else if (value.getClass().equals(Short[].class))
			return Arrays.asList((Short[]) value);
		else if (value.getClass().equals(Integer[].class))
			return Arrays.asList((Integer[]) value);
		else if (value.getClass().equals(Long[].class))
			return Arrays.asList((Long[]) value);
		else if (value.getClass().equals(Float[].class))
			return Arrays.asList((Float[]) value);
		else if (value.getClass().equals(Double[].class))
			return Arrays.asList((Double[]) value);
		else if (value.getClass().equals(Boolean[].class))
			return Arrays.asList((Boolean[]) value);
		else if (value.getClass().equals(Character[].class))
			return Arrays.asList((Character[]) value);

		throw new CannotMapFieldException();
	}

	@Override
	public boolean predicate(Field c) {
		return ReflectionUtils.isPrimitiveArrayOrBoxed(c.getType());
	}

}
