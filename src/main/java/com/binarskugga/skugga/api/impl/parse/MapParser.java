package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.FieldParser;
import com.binarskugga.skugga.api.annotation.UseParser;
import com.binarskugga.skugga.api.exception.InvalidFieldException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapParser {

	private MapParser() {}

	@SuppressWarnings("unchecked")
	public static Object parse(List<Field> fields, Map<String, Object> input) {
		Object instance = PrimitivaReflection.constructOrNull(fields.get(0).getDeclaringClass());
		if (instance != null) {
			try {
				for (Field f : fields) {
					FieldParsingHandler parsingHandler = FieldParsingHandler.get();
					FieldParser parser = parsingHandler.getParser(f, PrimitivaReflection.getFieldAnnotationOrNull(f, UseParser.class));

					Object value = input.get(f.getName());

					if(value != null) {
						if (Map.class.isAssignableFrom(value.getClass()) && !Map.class.isAssignableFrom(f.getType()))
							value = MapParser.parse(PrimitivaReflection.getAllFields(f.getType()), (Map<String, Object>) value);
						else if (parser != null)
							value = parser.parse(f, value);
					}

					PrimitivaReflection.setField(f, instance, value);
				}
			} catch (Exception e) {
				throw new InvalidFieldException();
			}
			return instance;
		} else return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> unparse(List<Field> fields, Object input) {
		Map<String, Object> map = new HashMap<>();
		try {
			for (Field f : fields) {
				FieldParsingHandler parsingHandler = FieldParsingHandler.get();
				FieldParser parser = parsingHandler.getParser(f, PrimitivaReflection.getFieldAnnotationOrNull(f, UseParser.class));

				Object value = PrimitivaReflection.getField(f, input);
				if (value != null && parser != null)
					value = parser.unparse(f, value);

				map.putIfAbsent(f.getName(), value);
			}
		} catch (Exception e) {
			throw new InvalidFieldException();
		}
		return map;
	}

}
