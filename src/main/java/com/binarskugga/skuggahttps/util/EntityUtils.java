package com.binarskugga.skuggahttps.util;

import com.binarskugga.skuggahttps.api.AuthentifiableEntity;
import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.api.annotation.Creatable;
import com.binarskugga.skuggahttps.api.annotation.CreateField;
import com.binarskugga.skuggahttps.api.annotation.Updatable;
import com.binarskugga.skuggahttps.api.annotation.UpdateField;
import com.binarskugga.skuggahttps.api.enums.InclusionMode;
import com.binarskugga.skuggahttps.api.exception.entity.EntityNotUpdatableException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {

	private EntityUtils() {}

	public static boolean isCreatable(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		String role = logged == null ? null : logged.getRoleName();
		Creatable creatable = ReflectionUtils.getClassAnnotationOrNull(entityClass, Creatable.class);
		if(creatable != null) {
			List<String> roles = Arrays.asList(creatable.roles());
			return roles.contains("*") || roles.contains(role);
		}
		return false;
	}

	public static boolean isUpdatable(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		String role = logged == null ? null : logged.getRoleName();
		Updatable updatable = ReflectionUtils.getClassAnnotationOrNull(entityClass, Updatable.class);
		if(updatable != null) {
			List<String> roles = Arrays.asList(updatable.roles());
			return roles.contains("*") || roles.contains(role);
		}
		return false;
	}

	public static List<Field> getUpdateFields(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if(!isUpdatable(entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		Updatable updatable = entityClass.getAnnotation(Updatable.class);
		List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
		if(updatable.inclusion() == InclusionMode.INCLUDE) {
			fields = fields.stream()
					.filter(f -> !f.isAnnotationPresent(UpdateField.class) || f.getAnnotation(UpdateField.class).inclusion() != InclusionMode.EXCLUDE)
					.collect(Collectors.toList());
		} else if(updatable.inclusion() == InclusionMode.EXCLUDE) {
			fields = fields.stream()
					.filter(f -> f.isAnnotationPresent(UpdateField.class) && f.getAnnotation(UpdateField.class).inclusion() == InclusionMode.INCLUDE)
					.collect(Collectors.toList());
		}


		fields = fields.stream().filter( f -> {
			if(!f.isAnnotationPresent(UpdateField.class)) return true;
			else {
				String role = logged == null ? null : logged.getRoleName();
				List<String> updateFieldRoles = Arrays.asList(f.getAnnotation(UpdateField.class).roles());
				return updateFieldRoles.contains("*") || updateFieldRoles.contains(role);
			}
		}).collect(Collectors.toList());

		return fields;
	}

	public static List<Field> getCreateFields(Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if(!isCreatable(entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		Creatable creatable = entityClass.getAnnotation(Creatable.class);
		List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
		if(creatable.inclusion() == InclusionMode.INCLUDE) {
			fields = fields.stream()
					.filter(f -> !f.isAnnotationPresent(CreateField.class) || f.getAnnotation(CreateField.class).inclusion() != InclusionMode.EXCLUDE)
					.collect(Collectors.toList());
		} else if(creatable.inclusion() == InclusionMode.EXCLUDE) {
			fields = fields.stream()
					.filter(f -> f.isAnnotationPresent(CreateField.class) && f.getAnnotation(CreateField.class).inclusion() == InclusionMode.INCLUDE)
					.collect(Collectors.toList());
		}

		fields = fields.stream().filter( f -> {
			if(!f.isAnnotationPresent(CreateField.class)) return true;
			else {
				String role = logged == null ? null : logged.getRoleName();
				List<String> createFieldRoles = Arrays.asList(f.getAnnotation(CreateField.class).roles());
				return createFieldRoles.contains("*") || createFieldRoles.contains(role);
			}
		}).collect(Collectors.toList());

		return fields;
	}

}
