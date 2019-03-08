package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.primitiva.conversion.PrimitivaArrayConverter;
import com.binarskugga.primitiva.conversion.PrimitivaConversion;
import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import com.eatthepath.uuid.FastUUID;
import com.google.common.base.Joiner;
import org.bson.types.ObjectId;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionParser implements ParameterParser<Collection> {

	@Override
	@SuppressWarnings("unchecked")
	public Collection parse(Parameter parameter, String argument) throws Exception {
		if(Collection.class.isAssignableFrom(parameter.getType())) {
			Class inner = (Class) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
			if (PrimitivaReflection.isBoxedPrimitive(inner)) {
				PrimitivaArrayConverter<String> converter = PrimitivaConversion.array(String.class).setSeparator(",");
				return Arrays.asList((Object[]) converter.convertTo(PrimitivaReflection.primitiveArrayOf(inner), argument));
			} else if (CharSequence.class.isAssignableFrom(inner)) {
				return Arrays.asList(argument.split(","));
			} else if(inner.equals(Class.class)) {
				String[] split = argument.split(",");
				return Stream.of(split).map(PrimitivaReflection::forNameOrNull).filter(Objects::nonNull).collect(Collectors.toList());
			} else if(inner.equals(ObjectId.class)) {
				String[] split = argument.split(",");
				return Stream.of(split).map(s -> {
					try { return new ObjectId(s); }
					catch (Exception e) { return null; }
				}).filter(Objects::nonNull).collect(Collectors.toList());
			} else if(inner.equals(UUID.class)) {
				String[] split = argument.split(",");
				return Stream.of(split).map(s -> {
					try { return FastUUID.parseUUID(s); }
					catch (Exception e) { return null; }
				}).filter(Objects::nonNull).collect(Collectors.toList());
			} else if(Enum.class.isAssignableFrom(inner)) {
				String[] split = argument.split(",");
				return (Collection) Stream.of(split).map(s -> {
					try { return Enum.valueOf(inner, s); }
					catch (Exception e) { return null; }
				}).filter(Objects::nonNull).collect(Collectors.toList());
			}
		}

		throw new CannotMapFieldException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String unparse(Parameter parameter, Collection collection) throws Exception {
		Class inner = (Class) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
		if (PrimitivaReflection.isBoxedPrimitive(inner)) {
			return Joiner.on(",").join((List<String>) collection.stream().map(Object::toString).collect(Collectors.toList()));
		} else if (CharSequence.class.isAssignableFrom(inner)) {
			return Joiner.on(",").join(collection);
		} else if(inner.equals(Class.class)) {
			return Joiner.on(",").join((List<String>) collection.stream().map(o -> ((Class) o).getName()).collect(Collectors.toList()));
		} else if(inner.equals(ObjectId.class)) {
			return Joiner.on(",").join((List<String>) collection.stream().map(o -> ((ObjectId) o).toHexString()).collect(Collectors.toList()));
		} else if(inner.equals(UUID.class)) {
			return Joiner.on(",").join((List<String>) collection.stream().map(o -> FastUUID.toString((UUID) o)).collect(Collectors.toList()));
		} else if(Enum.class.isAssignableFrom(inner)) {
			return Joiner.on(",").join((List<String>) collection.stream().map(o -> ((Enum) o).name()).collect(Collectors.toList()));
		}

		throw new CannotMapFieldException();
	}

	@Override
	public boolean predicate(Parameter c) {
		if(Collection.class.isAssignableFrom(c.getType())) {
			Class inner = (Class) ((ParameterizedType) c.getParameterizedType()).getActualTypeArguments()[0];
			return PrimitivaReflection.isBoxedPrimitive(inner) || CharSequence.class.isAssignableFrom(inner)
					|| inner.equals(Class.class) || inner.equals(ObjectId.class) || inner.equals(UUID.class)
					|| Enum.class.isAssignableFrom(inner);
		}

		return false;
	}

}
