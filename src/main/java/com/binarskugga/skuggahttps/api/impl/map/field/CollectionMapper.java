package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.FieldMapper;
import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionMapper implements FieldMapper<Collection, Collection> {

	@Override
	public Collection toMap(Field field, Collection value) throws CannotMapFieldException {
		return value;
	}

	@Override
	public Collection toEntity(Field field, Collection value) throws CannotMapFieldException {
		return value;
	}

}
