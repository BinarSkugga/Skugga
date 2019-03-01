package com.binarskugga.skugga.util;

import com.binarskugga.skugga.api.exception.ReflectiveContructFailedException;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtils {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	private ReflectionUtils() {
	}

	public static Class forNameOrNull(String str) {
		try {
			return Class.forName(str);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static List<Field> getAllFields(Class clazz) {
		List<Field> fields = new ArrayList<>();
		while (clazz.getSuperclass() != null) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	public static List<Method> getAllMethods(Class clazz) {
		List<Method> methods = new ArrayList<>();
		while (clazz.getSuperclass() != null) {
			methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getClassAnnotationOrNull(Class clazz, Class<T> annotation) {
		if (!clazz.isAnnotationPresent(annotation)) return null;
		else return (T) clazz.getAnnotation(annotation);
	}

	public static <T extends Annotation> T getMethodAnnotationOrNull(Method method, Class<T> annotation) {
		if (!method.isAnnotationPresent(annotation)) return null;
		else return method.getAnnotation(annotation);
	}

	public static <T extends Annotation> T getFieldAnnotationOrNull(Field field, Class<T> annotation) {
		if (!field.isAnnotationPresent(annotation)) return null;
		else return field.getAnnotation(annotation);
	}

	public static <T extends Annotation> T getParamAnnotationOrNull(Parameter param, Class<T> annotation) {
		if (!param.isAnnotationPresent(annotation)) return null;
		else return param.getAnnotation(annotation);
	}

	public static <T extends Annotation> T getAnnotationOrNull(Object reflect, Class<T> annotation) {
		if (Class.class.isAssignableFrom(reflect.getClass()))
			return ReflectionUtils.getClassAnnotationOrNull((Class) reflect, annotation);
		else if (Method.class.isAssignableFrom(reflect.getClass()))
			return ReflectionUtils.getMethodAnnotationOrNull((Method) reflect, annotation);
		else if (Field.class.isAssignableFrom(reflect.getClass()))
			return ReflectionUtils.getFieldAnnotationOrNull((Field) reflect, annotation);
		else if (Parameter.class.isAssignableFrom(reflect.getClass()))
			return ReflectionUtils.getParamAnnotationOrNull((Parameter) reflect, annotation);
		return null;
	}

	public static <T> T constructOrNull(Class<T> declaring, Object... arguments) {
		try {
			return safeConstruct(declaring, arguments);
		} catch (ReflectiveContructFailedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T safeConstruct(Class<T> declaring, Object... arguments) throws ReflectiveContructFailedException {
		try {
			Constructor<T> constructor = null;
			if (arguments == null || arguments.length == 0)
				constructor = declaring.getConstructor();
			else {
				constructor = declaring.getConstructor(Arrays.asList(arguments).stream()
						.map(Object::getClass).collect(Collectors.toList()).toArray(new Class[arguments.length]));
			}

			constructor.setAccessible(true);
			return constructor.newInstance(arguments);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ReflectiveContructFailedException();
		}
	}

	public static void setField(Field field, Object instance, Object value, boolean force) {
		try {
			if (force) field.setAccessible(true);
			field.set(instance, value);
		} catch (Exception ignored) {
		}
	}

	public static void setField(Field field, Object instance, Object value) {
		setField(field, instance, value, true);
	}

	public static Object getField(Field field, Object instance, boolean force) {
		try {
			if (force) field.setAccessible(true);
			return field.get(instance);
		} catch (Exception ignored) {
			return null;
		}
	}

	public static Object getField(Field field, Object instance) {
		return getField(field, instance, true);
	}


	public static <T> T invokeOrNull(Class<T> returnClass, Method action, Object instance, Object[] arguments) {
		try {
			return (T) action.invoke(instance, arguments);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean typeEqualsIgnoreBoxing(Class clazz, Class boxed, Class unboxed) {
		return clazz.equals(boxed) || clazz.equals(unboxed);
	}

	public static boolean typeEqualsIgnoreBoxing(Class clazz, Class clazz2, Class boxed, Class unboxed) {
		return (clazz.equals(boxed) || clazz.equals(unboxed)) && (clazz2.equals(boxed) || clazz2.equals(unboxed));
	}

	public static Class getInnerArrayType(Class arrayClass) {
		if (arrayClass.isArray())
			return arrayClass.getComponentType();
		return arrayClass;
	}

	public static boolean isPrimitive(Class clazz) {
		return clazz.equals(byte.class) || clazz.equals(short.class) || clazz.equals(int.class)
				|| clazz.equals(long.class) || clazz.equals(double.class) || clazz.equals(float.class)
				|| clazz.equals(char.class) || clazz.equals(boolean.class);
	}

	public static boolean isBoxedPrimitive(Class clazz) {
		return Number.class.isAssignableFrom(clazz) || clazz.equals(Character.class) || clazz.equals(Boolean.class);
	}

	public static boolean isPrimitiveOrBoxed(Class clazz) {
		return isPrimitive(clazz) || isBoxedPrimitive(clazz);
	}

	public static boolean isPrimitiveArray(Class clazz) {
		return clazz.equals(byte[].class) || clazz.equals(short[].class) || clazz.equals(int[].class)
				|| clazz.equals(long[].class) || clazz.equals(double[].class) || clazz.equals(float[].class)
				|| clazz.equals(char[].class) || clazz.equals(boolean[].class);
	}

	public static boolean isBoxedPrimitiveArray(Class clazz) {
		return clazz.equals(Byte[].class) || clazz.equals(Short[].class) || clazz.equals(Integer[].class)
				|| clazz.equals(Long[].class) || clazz.equals(Double[].class) || clazz.equals(Float[].class)
				|| clazz.equals(Character[].class) || clazz.equals(Boolean[].class);
	}

	public static boolean isPrimitiveArrayOrBoxed(Class clazz) {
		return isPrimitiveArray(clazz) || isBoxedPrimitiveArray(clazz);
	}

	public static boolean isNumericPrimitive(Class clazz) {
		return clazz.equals(byte.class) || clazz.equals(short.class) || clazz.equals(int.class)
				|| clazz.equals(long.class) || clazz.equals(double.class) || clazz.equals(float.class);
	}

	public static boolean isNumericPrimitiveOrBoxed(Class clazz) {
		return isNumericPrimitive(clazz) || Number.class.isAssignableFrom(clazz);
	}

	public static boolean isFloatingNumericPrimitive(Class clazz) {
		return clazz.equals(double.class) || clazz.equals(float.class);
	}

	public static boolean isIntegerNumericPrimitive(Class clazz) {
		return clazz.equals(byte.class) || clazz.equals(short.class) || clazz.equals(int.class)
				|| clazz.equals(long.class);
	}

	public static Class unboxClass(Class clazz) {
		if(!isBoxedPrimitive(clazz) && !isBoxedPrimitiveArray(clazz)) return null;
		else if(clazz.equals(Boolean.class)) return boolean.class;
		else if(clazz.equals(Character.class)) return char.class;
		else if(clazz.equals(Double.class)) return double.class;
		else if(clazz.equals(Float.class)) return float.class;
		else if(clazz.equals(Long.class)) return long.class;
		else if(clazz.equals(Integer.class)) return int.class;
		else if(clazz.equals(Short.class)) return short.class;
		else if(clazz.equals(Byte.class)) return byte.class;
		else if(clazz.equals(Boolean[].class)) return boolean[].class;
		else if(clazz.equals(Character[].class)) return char[].class;
		else if(clazz.equals(Double[].class)) return double[].class;
		else if(clazz.equals(Float[].class)) return float[].class;
		else if(clazz.equals(Long[].class)) return long[].class;
		else if(clazz.equals(Integer[].class)) return int[].class;
		else if(clazz.equals(Short[].class)) return short[].class;
		else if(clazz.equals(Byte[].class)) return byte[].class;
		else return null;
	}

	public static Class boxClass(Class clazz) {
		if(!isPrimitive(clazz) && !isPrimitiveArray(clazz)) return null;
		if(clazz.equals(boolean.class)) return Boolean.class;
		else if(clazz.equals(char.class)) return Character.class;
		else if(clazz.equals(double.class)) return Double.class;
		else if(clazz.equals(float.class)) return Float.class;
		else if(clazz.equals(long.class)) return Long.class;
		else if(clazz.equals(int.class)) return Integer.class;
		else if(clazz.equals(short.class)) return Short.class;
		else if(clazz.equals(byte.class)) return Byte.class;
		else if(clazz.equals(boolean[].class)) return Boolean[].class;
		else if(clazz.equals(char[].class)) return Character[].class;
		else if(clazz.equals(double[].class)) return Double[].class;
		else if(clazz.equals(float[].class)) return Float[].class;
		else if(clazz.equals(long[].class)) return Long[].class;
		else if(clazz.equals(int[].class)) return Integer[].class;
		else if(clazz.equals(short[].class)) return Short[].class;
		else if(clazz.equals(byte[].class)) return Byte[].class;
		else return null;
	}

	public static Object stringToPrimitive(String str, Class c) {
		if (isPrimitiveOrBoxed(c)) {
			if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Byte.class, byte.class))
				return Byte.parseByte(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Short.class, short.class))
				return Short.parseShort(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Integer.class, int.class))
				return Integer.parseInt(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Long.class, long.class))
				return Long.parseLong(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Float.class, float.class))
				return Float.parseFloat(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Double.class, double.class))
				return Double.parseDouble(str);
			else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Boolean.class, boolean.class)) {
				if(!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false"))
					logger.atWarning().log("Sending a value other than true or false to boolean type, assuming false.");
				return str.equalsIgnoreCase("true");
			} else if (ReflectionUtils.typeEqualsIgnoreBoxing(c, Character.class, char.class)) {
				if(str.length() > 1)
					logger.atWarning().log("Sending a string with more that one character to a char type.");
				return str.charAt(0);
			} else
				return str;
		} else return str;
	}

	public static Object stringToPrimitiveArray(String str, String separator, Class c) {
		if (c.equals(byte[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Byte::parseByte).toArray(Byte[]::new));
		else if (c.equals(short[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Short::parseShort).toArray(Short[]::new));
		else if (c.equals(int[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Integer::parseInt).toArray(Integer[]::new));
		else if (c.equals(long[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Long::parseLong).toArray(Long[]::new));
		else if (c.equals(float[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Float::parseFloat).toArray(Float[]::new));
		else if (c.equals(double[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(Double::parseDouble).toArray(Double[]::new));
		else if (c.equals(boolean[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(b -> {
				if(!b.equalsIgnoreCase("true") && !b.equalsIgnoreCase("false"))
					logger.atWarning().log("Sending a value other than true or false to boolean type, assuming false.");
				return b.equalsIgnoreCase("true");
			}).toArray(Boolean[]::new));
		else if (c.equals(char[].class))
			return ArrayUtils.toPrimitive(Stream.of(str.split(separator)).map(ch -> {
				if(ch.length() > 1)
					logger.atWarning().log("Sending a string with more that one character to a char type.");
				return ch.charAt(0);
			}).toArray(Character[]::new));
		else if (c.equals(Byte[].class))
			return Stream.of(str.split(separator)).map(Byte::parseByte).toArray(Byte[]::new);
		else if (c.equals(Short[].class))
			return Stream.of(str.split(separator)).map(Short::parseShort).toArray(Short[]::new);
		else if (c.equals(Integer[].class))
			return Stream.of(str.split(separator)).map(Integer::parseInt).toArray(Integer[]::new);
		else if (c.equals(Long[].class))
			return Stream.of(str.split(separator)).map(Long::parseLong).toArray(Long[]::new);
		else if (c.equals(Float[].class))
			return Stream.of(str.split(separator)).map(Float::parseFloat).toArray(Float[]::new);
		else if (c.equals(Double[].class))
			return Stream.of(str.split(separator)).map(Double::parseDouble).toArray(Double[]::new);
		else if (c.equals(Boolean[].class))
			return Stream.of(str.split(separator)).map(b -> {
				if(!b.equalsIgnoreCase("true") && !b.equalsIgnoreCase("false"))
					logger.atWarning().log("Sending a value other than true or false to boolean type, assuming false.");
				return b.equalsIgnoreCase("true");
			}).toArray(Boolean[]::new);
		else if (c.equals(Character[].class))
			return Stream.of(str.split(separator)).map(ch -> {
				if(ch.length() > 1)
					logger.atWarning().log("Sending a string with more that one character to a char type.");
				return ch.charAt(0);
			}).toArray(Character[]::new);
		else
			return str;
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> stringToPrimitiveCollection(String str, String separator, Class<T> inner) {
		if (inner.equals(Byte.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Byte::parseByte).collect(Collectors.toList());
		else if (inner.equals(Short.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Short::parseShort).collect(Collectors.toList());
		else if (inner.equals(Integer.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Integer::parseInt).collect(Collectors.toList());
		else if (inner.equals(Long.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Long::parseLong).collect(Collectors.toList());
		else if (inner.equals(Float.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Float::parseFloat).collect(Collectors.toList());
		else if (inner.equals(Double.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(Double::parseDouble).collect(Collectors.toList());
		else if (inner.equals(Boolean.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(b -> b.equalsIgnoreCase("true")).collect(Collectors.toList());
		else if (inner.equals(Character.class))
			return (Collection<T>) Stream.of(str.split(separator)).map(ch -> ch.charAt(0)).collect(Collectors.toList());
		else
			return null;
	}

}
