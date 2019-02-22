package com.binarskugga.skuggahttps.api.impl.map.field;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.exception.*;

import java.lang.reflect.*;
import java.util.*;

public class UUIDMapper implements FieldMapper<String, UUID> {

	@Override
	public String toMap(Field field, UUID value) throws CannotMapFieldException {
		return value.toString();
	}

	@Override
	public UUID toEntity(Field field, String value) throws CannotMapFieldException {
		return UUID.fromString(value);
	}

}
