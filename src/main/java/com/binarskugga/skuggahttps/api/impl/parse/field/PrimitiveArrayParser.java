package com.binarskugga.skuggahttps.api.impl.parse.field;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class PrimitiveArrayParser implements FieldParser<Object, Object> {

	@Override
	public Object parse(Field field, Object value) throws CannotMapFieldException {
		if (Collection.class.isAssignableFrom(value.getClass())) {
			if (field.getType().equals(byte[].class))
				return ArrayUtils.toPrimitive(((Collection<Byte>) value).toArray(new Byte[0]));
			else if (field.getType().equals(short[].class))
				return ArrayUtils.toPrimitive(((Collection<Short>) value).toArray(new Short[0]));
			else if (field.getType().equals(int[].class))
				return ArrayUtils.toPrimitive(((Collection<Integer>) value).toArray(new Integer[0]));
			else if (field.getType().equals(long[].class))
				return ArrayUtils.toPrimitive(((Collection<Long>) value).toArray(new Long[0]));
			else if (field.getType().equals(float[].class))
				return ArrayUtils.toPrimitive(((Collection<Float>) value).toArray(new Float[0]));
			else if (field.getType().equals(double[].class))
				return ArrayUtils.toPrimitive(((Collection<Double>) value).toArray(new Double[0]));
			else if (field.getType().equals(boolean[].class))
				return ArrayUtils.toPrimitive(((Collection<Boolean>) value).toArray(new Boolean[0]));
			else if (field.getType().equals(char[].class))
				return ArrayUtils.toPrimitive(((Collection<Character>) value).toArray(new Character[0]));
			else if (field.getType().equals(Byte[].class))
				return ((Collection<Byte>) value).toArray(new Byte[0]);
			else if (field.getType().equals(Short[].class))
				return ((Collection<Short>) value).toArray(new Short[0]);
			else if (field.getType().equals(Integer[].class))
				return ((Collection<Integer>) value).toArray(new Integer[0]);
			else if (field.getType().equals(Long[].class))
				return ((Collection<Long>) value).toArray(new Long[0]);
			else if (field.getType().equals(Float[].class))
				return ((Collection<Float>) value).toArray(new Float[0]);
			else if (field.getType().equals(Double[].class))
				return ((Collection<Double>) value).toArray(new Double[0]);
			else if (field.getType().equals(Boolean[].class))
				return ((Collection<Boolean>) value).toArray(new Boolean[0]);
			else if (field.getType().equals(Character[].class))
				return ((Collection<Character>) value).toArray(new Character[0]);
		}

		throw new CannotMapFieldException();
	}

	@Override
	@SuppressWarnings("unchecked")
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
