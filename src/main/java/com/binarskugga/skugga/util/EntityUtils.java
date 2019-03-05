package com.binarskugga.skugga.util;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.api.AuthentifiableEntity;
import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.annotation.*;
import com.binarskugga.skugga.api.enums.InclusionMode;
import com.binarskugga.skugga.api.exception.entity.EntityNotCreatableException;
import com.binarskugga.skugga.api.exception.entity.EntityNotObtainableException;
import com.binarskugga.skugga.api.exception.entity.EntityNotUpdatableException;
import com.binarskugga.skugga.api.impl.parse.MapParser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityUtils {

	private EntityUtils() {}

	public static boolean isCreatable(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		String role = logged == null ? null : logged.getRoleName();
		Creatable creatable = PrimitivaReflection.getClassAnnotationOrNull(entityClass, Creatable.class);
		if (creatable != null) {
			List<String> roles = Arrays.asList(creatable.roles());
			return roles.contains("*") || roles.contains(role);
		}
		return false;
	}

	public static boolean isUpdatable(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		String role = logged == null ? null : logged.getRoleName();
		Updatable updatable = PrimitivaReflection.getClassAnnotationOrNull(entityClass, Updatable.class);
		if (updatable != null) {
			List<String> roles = Arrays.asList(updatable.roles());
			return roles.contains("*") || roles.contains(role);
		}
		return false;
	}

	public static boolean isObtainable(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		String role = logged == null ? null : logged.getRoleName();
		Obtainable obtainable = PrimitivaReflection.getClassAnnotationOrNull(entityClass, Obtainable.class);
		if (obtainable != null) {
			List<String> roles = Arrays.asList(obtainable.roles());
			return roles.contains("*") || roles.contains(role);
		}
		return false;
	}

	public static List<Field> getUpdateFields(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isUpdatable(entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		Updatable updatable = entityClass.getAnnotation(Updatable.class);
		List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
		if (updatable.inclusion() == InclusionMode.INCLUDE) {
			fields = fields.stream()
					.filter(f -> !f.isAnnotationPresent(UpdateField.class) || f.getAnnotation(UpdateField.class).inclusion() != InclusionMode.EXCLUDE)
					.collect(Collectors.toList());
		} else if (updatable.inclusion() == InclusionMode.EXCLUDE) {
			fields = fields.stream()
					.filter(f -> f.isAnnotationPresent(UpdateField.class) && f.getAnnotation(UpdateField.class).inclusion() == InclusionMode.INCLUDE)
					.collect(Collectors.toList());
		}


		fields = fields.stream().filter(f -> {
			if (!f.isAnnotationPresent(UpdateField.class)) return true;
			else {
				String role = logged == null ? null : logged.getRoleName();
				List<String> updateFieldRoles = Arrays.asList(f.getAnnotation(UpdateField.class).roles());
				return updateFieldRoles.contains("*") || updateFieldRoles.contains(role);
			}
		}).collect(Collectors.toList());

		return fields;
	}

	public static List<Field> getCreateFields(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isCreatable(entityClass, logged))
			throw new EntityNotCreatableException(entityClass);

		Creatable creatable = entityClass.getAnnotation(Creatable.class);
		List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
		if (creatable.inclusion() == InclusionMode.INCLUDE) {
			fields = fields.stream()
					.filter(f -> !f.isAnnotationPresent(CreateField.class) || f.getAnnotation(CreateField.class).inclusion() != InclusionMode.EXCLUDE)
					.collect(Collectors.toList());
		} else if (creatable.inclusion() == InclusionMode.EXCLUDE) {
			fields = fields.stream()
					.filter(f -> f.isAnnotationPresent(CreateField.class) && f.getAnnotation(CreateField.class).inclusion() == InclusionMode.INCLUDE)
					.collect(Collectors.toList());
		}

		fields = fields.stream().filter(f -> {
			if (!f.isAnnotationPresent(CreateField.class)) return true;
			else {
				String role = logged == null ? null : logged.getRoleName();
				List<String> createFieldRoles = Arrays.asList(f.getAnnotation(CreateField.class).roles());
				return createFieldRoles.contains("*") || createFieldRoles.contains(role);
			}
		}).collect(Collectors.toList());

		return fields;
	}

	public static List<Field> getObtainFields(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isObtainable(entityClass, logged))
			throw new EntityNotObtainableException(entityClass);

		Obtainable obtainable = entityClass.getAnnotation(Obtainable.class);
		List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
		if (obtainable.inclusion() == InclusionMode.INCLUDE) {
			fields = fields.stream()
					.filter(f -> !f.isAnnotationPresent(ObtainField.class) || f.getAnnotation(ObtainField.class).inclusion() != InclusionMode.EXCLUDE)
					.collect(Collectors.toList());
		} else if (obtainable.inclusion() == InclusionMode.EXCLUDE) {
			fields = fields.stream()
					.filter(f -> f.isAnnotationPresent(ObtainField.class) && f.getAnnotation(ObtainField.class).inclusion() == InclusionMode.INCLUDE)
					.collect(Collectors.toList());
		}

		fields = fields.stream().filter(f -> {
			if (!f.isAnnotationPresent(ObtainField.class)) return true;
			else {
				String role = logged == null ? null : logged.getRoleName();
				List<String> obtainFieldRoles = Arrays.asList(f.getAnnotation(ObtainField.class).roles());
				return obtainFieldRoles.contains("*") || obtainFieldRoles.contains(role);
			}
		}).collect(Collectors.toList());

		return fields;
	}

	public static <T extends BaseEntity> Map<String, Object> obtain(Class<T> entityClass, T e, AuthentifiableEntity logged) {
		return MapParser.unparse(getObtainFields(entityClass, logged), e);
	}

	public static <T extends BaseEntity> Map<String, Object> obtain(Class<T> entityClass, T e) {
		return obtain(entityClass, e, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T create(Class<T> entityClass, Map<String, Object> e, AuthentifiableEntity logged) {
		return (T) MapParser.parse(getCreateFields(entityClass, logged), e);
	}

	public static <T extends BaseEntity> T create(Class<T> entityClass, Map<String, Object> e) {
		return create(entityClass, e, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T update(Class<T> entityClass, Map<String, Object> e, AuthentifiableEntity logged) {
		return (T) MapParser.parse(getUpdateFields(entityClass, logged), e);
	}

	public static <T extends BaseEntity> T update(Class<T> entityClass, Map<String, Object> e) {
		return update(entityClass, e, null);
	}


}
