package com.binarskugga.skuggahttps.api;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;

public interface EndpointMapper<T> {

	Object toEntity(List<Field> fields, T input);
	T toMap(List<Field> fields, Object input);
	Object toParameter(Parameter parameter, String str);

}
