package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.annotation.UseParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapParser {

	private MapParser() {
	}

	@SuppressWarnings("unchecked")
	public static Object parse(List<Field> fields, Map<String, Object> input) throws RuntimeException {
		Object instance = ReflectionUtils.constructOrNull(fields.get(0).getDeclaringClass());
		if (instance != null) {
			for (Field f : fields) {
				FieldParsingHandler parsingHandler = FieldParsingHandler.get();
				FieldParser parser = parsingHandler.getParser(f, ReflectionUtils.getFieldAnnotationOrNull(f, UseParser.class));

				Object value = input.get(f.getName());
				if (value != null && parser != null)
					value = parser.parse(f, value);

				ReflectionUtils.setField(f, instance, value);
			}
			return instance;
		} else return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> unparse(List<Field> fields, Object input) throws RuntimeException {
		Map<String, Object> map = new HashMap<>();
		for (Field f : fields) {
			FieldParsingHandler parsingHandler = FieldParsingHandler.get();
			FieldParser parser = parsingHandler.getParser(f, ReflectionUtils.getFieldAnnotationOrNull(f, UseParser.class));

			Object value = ReflectionUtils.getField(f, input);
			if (value != null && parser != null)
				value = parser.unparse(f, value);

			map.putIfAbsent(f.getName(), value);
		}
		return map;
	}

}
