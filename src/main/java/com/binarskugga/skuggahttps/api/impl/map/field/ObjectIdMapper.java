package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

public class ObjectIdMapper implements FieldMapper<String, ObjectId> {

	@Override
	public String toMap(Field field, ObjectId value) throws CannotMapFieldException {
		return value.toHexString();
	}

	@Override
	public ObjectId toEntity(Field field, String value) throws CannotMapFieldException {
		return new ObjectId(value);
	}

}
