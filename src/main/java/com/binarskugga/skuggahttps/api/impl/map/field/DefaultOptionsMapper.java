package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.exception.*;

import java.lang.reflect.*;

public class DefaultOptionsMapper implements FieldMapper {

	@Override
	public Object toMap(Field field, Object value) throws CannotMapFieldException {
		throw new CannotMapFieldException();
	}

	@Override
	public Object toEntity(Field field, Object value) throws CannotMapFieldException {
		throw new CannotMapFieldException();
	}

}
