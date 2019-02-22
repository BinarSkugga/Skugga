package com.binarskugga.skuggahttps.api.impl.map;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.annotation.MappingOptions;
import com.binarskugga.skuggahttps.api.annotation.ParamParser;
import com.binarskugga.skuggahttps.api.impl.map.field.*;
import com.binarskugga.skuggahttps.api.impl.map.param.*;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

public class DefaultEndpointMapper implements EndpointMapper<Map<String, Object>> {

	@Getter private Map<FieldMapper, Predicate<Class>> fieldMappers;
	@Getter private Map<ParameterParser, Predicate<Class>> parameterParsers;

	public DefaultEndpointMapper() {
		this.fieldMappers = new HashMap<>();
		this.fieldMappers.put(new PrimitiveMapper(), ReflectionUtils::isPrimitiveOrBoxed);
		this.fieldMappers.put(new PrimitiveArrayMapper(), ReflectionUtils::isPrimitiveArrayOrBoxed);
		this.fieldMappers.put(new StringMapper(), CharSequence.class::isAssignableFrom);
		this.fieldMappers.put(new StringArrayMapper(), c -> c.equals(CharSequence[].class) || c.equals(String[].class));
		this.fieldMappers.put(new ClassMapper(), Class.class::equals);
		this.fieldMappers.put(new CollectionMapper(), Collection.class::isAssignableFrom);
		this.fieldMappers.put(new UUIDMapper(), UUID.class::equals);
		this.fieldMappers.put(new ObjectIdMapper(), ObjectId.class::equals);

		this.parameterParsers = new HashMap<>();
		this.parameterParsers.put(new PrimitiveParser(), ReflectionUtils::isPrimitiveOrBoxed);
		this.parameterParsers.put(new PrimitveArrayParser(), ReflectionUtils::isPrimitiveArrayOrBoxed);
		this.parameterParsers.put(new CollectionParser(), Collection.class::isAssignableFrom);
		this.parameterParsers.put(new StringParser(), CharSequence.class::isAssignableFrom);
		this.parameterParsers.put(new StringArrayParser(), c -> c.equals(CharSequence[].class) || c.equals(String[].class));
		this.parameterParsers.put(new ClassParser(), Class.class::equals);
		this.parameterParsers.put(new UUIDParser(), UUID.class::equals);
		this.parameterParsers.put(new ObjectIdParser(), ObjectId.class::equals);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object toEntity(List<Field> fields, Map<String, Object> input) throws RuntimeException {
		Object instance = ReflectionUtils.constructOrNull(fields.get(0).getDeclaringClass());
		if(instance != null) {
			for(Field f : fields) {
				MappingOptions options = ReflectionUtils.getFieldAnnotationOrNull(f, MappingOptions.class);
				String name = options == null || options.name().equals("") ? f.getName() : options.name();
				FieldMapper mapper = this.getFieldMapper(f);

				Object value = input.get(name);
				if(value != null && mapper != null)
					value = mapper.toEntity(f, value);

				Class<? extends MappingPredicate>[] predicates = null;
				if(options != null) predicates = options.predicates();
				if(predicates != null && predicates.length > 0) {
					boolean predicated = true;
					for(Class<? extends MappingPredicate> pClass : predicates) {
						MappingPredicate p = ReflectionUtils.constructOrNull(pClass);
						predicated &= p.applyToEntity(value, input);
					}
					if(!predicated) continue;
				}

				ReflectionUtils.setField(f, instance, value);
			}
			return instance;
		} else return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap(List<Field> fields, Object input) throws RuntimeException {
		Map<String, Object> map = new HashMap<>();
		for(Field f : fields) {
			MappingOptions options = ReflectionUtils.getFieldAnnotationOrNull(f, MappingOptions.class);
			String name = options == null || options.name().equals("") ? f.getName() : options.name();
			FieldMapper mapper = this.getFieldMapper(f);

			Object value = ReflectionUtils.getField(f, input);
			if(value != null && mapper != null)
				value = mapper.toMap(f, value);

			Class<? extends MappingPredicate>[] predicates = null;
			if(options != null) predicates = options.predicates();
			if(predicates != null && predicates.length > 0) {
				boolean predicated = true;
				for(Class<? extends MappingPredicate> pClass : predicates) {
					MappingPredicate p = ReflectionUtils.constructOrNull(pClass);
					predicated &= p.applyToMap(value, input);
				}
				if(!predicated) continue;
			}

			map.putIfAbsent(name, value);
		}
		return map;
	}

	@Override
	public Object toParameter(Parameter parameter, String str) {
		ParameterParser parser = this.getParameterParser(parameter);
		return parser.parse(parameter, str);
	}

	private FieldMapper getFieldMapper(Field f) {
		FieldMapper defaultMapper = null;
		for(Map.Entry<FieldMapper, Predicate<Class>> entry : this.fieldMappers.entrySet())
			if(entry.getValue().test(f.getType())) defaultMapper = entry.getKey();

		FieldMapper mapper = defaultMapper;
		if(f.isAnnotationPresent(MappingOptions.class)) {
			Class<? extends FieldMapper> clazz = ReflectionUtils.getFieldAnnotationOrNull(f, MappingOptions.class).mapper();
			if(!clazz.equals(DefaultOptionsMapper.class)) {
				mapper = ReflectionUtils.constructOrNull(clazz);
				if(mapper == null) mapper = defaultMapper;
			}
		}
		return mapper;
	}

	private ParameterParser getParameterParser(Parameter p) {
		ParameterParser defaultParser = null;
		for(Map.Entry<ParameterParser, Predicate<Class>> entry : this.parameterParsers.entrySet())
			if(entry.getValue().test(p.getType())) defaultParser = entry.getKey();

		ParameterParser parser = defaultParser;
		if(p.isAnnotationPresent(ParamParser.class)) {
			Class<? extends ParameterParser> clazz = ReflectionUtils.getParamAnnotationOrNull(p, ParamParser.class).value();
			parser = ReflectionUtils.constructOrNull(clazz);
			if(parser == null) parser = defaultParser;
		}
		return parser;
	}

}
