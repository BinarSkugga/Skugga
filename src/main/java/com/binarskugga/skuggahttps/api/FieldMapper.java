package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.exception.CannotMapFieldException;

import java.lang.reflect.Field;

public interface FieldMapper<M, P> {


	M toMap(Field field, P value) throws CannotMapFieldException;
	P toEntity(Field field, M value) throws CannotMapFieldException;

}
