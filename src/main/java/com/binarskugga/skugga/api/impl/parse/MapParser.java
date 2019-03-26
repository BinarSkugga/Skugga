package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.primitiva.reflection.FieldReflector;
import com.binarskugga.primitiva.reflection.TypeReflector;
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
		TypeReflector<Class> reflector = Primitiva.Reflection.ofType(fields.get(0).getDeclaringClass());
		Object instance = reflector.create(fields.get(0).getDeclaringClass());
		if (instance != null) {
			try {
				for (Field f : fields) {
					FieldReflector fieldReflector = Primitiva.Reflection.ofField(f);
					FieldParsingHandler parsingHandler = FieldParsingHandler.get();
					FieldParser parser = parsingHandler.getParser(f, fieldReflector.getAnnotation(UseParser.class));

					Object value = input.get(f.getName());

					if(value != null) {
						if (Map.class.isAssignableFrom(value.getClass()) && !Map.class.isAssignableFrom(f.getType()) && parser == null)
							value = MapParser.parse(ClassTools.of(f.getType()).getAllFields(), (Map<String, Object>) value);
						else if (parser != null)
							value = parser.parse(f, value);
						else
							throw new InvalidFieldException();
					}

					fieldReflector.set(instance, value);
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
				FieldReflector fieldReflector = Primitiva.Reflection.ofField(f);
				FieldParsingHandler parsingHandler = FieldParsingHandler.get();
				FieldParser parser = parsingHandler.getParser(f, fieldReflector.getAnnotation(UseParser.class));

				Object value = fieldReflector.get(input);
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
