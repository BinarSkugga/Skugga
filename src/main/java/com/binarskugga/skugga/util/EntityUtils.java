package com.binarskugga.skugga.util;

import com.binarskugga.primitiva.ClassTools;
import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.skugga.api.*;
import com.binarskugga.skugga.api.annotation.Permission;
import com.binarskugga.skugga.api.annotation.Permissions;
import com.binarskugga.skugga.api.exception.entity.EntityNotUpdatableException;
import com.binarskugga.skugga.api.impl.parse.MapParser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EntityUtils {

	private EntityUtils() {}

	@SuppressWarnings("unchecked")
	private static Permission[] getPermissions(Class<? extends BaseEntity> clazz) {
		if(!clazz.isAnnotationPresent(Permissions.class)) return null;
		return Primitiva.Reflection.ofType(clazz).getAnnotation(Permissions.class).value();
	}

	private static Permission[] getFieldPermissions(Field field) {
		if(!field.isAnnotationPresent(Permissions.class)) return null;
		return Primitiva.Reflection.ofField(field).getAnnotation(Permissions.class).value();
	}

	private static boolean applyPredicates(Object e, Object v, Class<? extends PermissionPredicate>[] predicates) {
		boolean pass = true;
		for(Class<? extends PermissionPredicate> predicateClass : predicates) {
			PermissionPredicate predicate = Primitiva.Reflection.ofType(predicateClass).create();
			if(predicate == null) return false;
			else if(predicate instanceof ValuePredicate) pass &= predicate.test(v);
			else if(predicate instanceof EntityPredicate) pass &= predicate.test(e);
		}
		return pass;
	}

	private static Permission getCurrentPermission(Object e, Object v, Permission[] permissions) {
		for(Permission perm : permissions) {
			if(applyPredicates(e, v, perm.conditions())) return perm;
		}
		return null;
	}

	private static boolean accessible(String action, Object e, Object v, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		Permission[] perms = getPermissions(entityClass);
		if(perms == null) return false;

		String role = logged == null ? null : logged.getRoleName();
		Permission perm = getCurrentPermission(e, v, perms);

		if (perm != null) {
			if(!perm.value().contains(action.substring(0, 1))) return false;

			List<String> roles = Arrays.asList(perm.roles());
			return roles.contains("*") || roles.contains(role);
		} else return false;
	}

	private static boolean fieldAccessible(String action, Object e, Object v, Field field, AuthentifiableEntity logged) {
		Permission[] perms = getFieldPermissions(field);
		if(perms == null) return true;

		String role = logged == null ? null : logged.getRoleName();
		Permission perm = getCurrentPermission(e, v, perms);

		if (perm != null) {
			if(!perm.value().contains(action.substring(0, 1))) return false;

			List<String> roles = Arrays.asList(perm.roles());
			return roles.contains("*") || roles.contains(role);
		} else return false;
	}

	public static boolean isReadable(Object e, Object v, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		return accessible("r", e, v, entityClass, logged);
	}

	public static boolean isWritable(Object e, Object v, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		return accessible("w", e, v, entityClass, logged);
	}

	public static boolean isCreatable(Object e, Object v, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) {
		return accessible("c", e, v, entityClass, logged);
	}

	@SuppressWarnings("unchecked")
	public static List<Field> getReadableFields(Object e, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isReadable(e, null, entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		return ClassTools.of(entityClass).getFields(f -> {
			Object v = Primitiva.Reflection.ofField(f).get(e);
			return fieldAccessible("r", e, v, f, logged);
		});
	}

	@SuppressWarnings("unchecked")
	public static List<Field> getWritableFields(Object e, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isWritable(e, null, entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		return ClassTools.of(entityClass).getFields(f -> {
			Object v = Primitiva.Reflection.ofField(f).get(e);
			return fieldAccessible("w", e, v, f, logged);
		});
	}

	@SuppressWarnings("unchecked")
	public static List<Field> getCreatableFields(Object e, Class<? extends BaseEntity> entityClass, AuthentifiableEntity logged) throws RuntimeException {
		if (!isCreatable(e, null, entityClass, logged))
			throw new EntityNotUpdatableException(entityClass);

		return ClassTools.of(entityClass).getFields(f -> {
			Object v = Primitiva.Reflection.ofField(f).get(e);
			return fieldAccessible("c", e, v, f, logged);
		});
	}

	public static <T extends BaseEntity> Map<String, Object> read(Class<T> entityClass, T e, AuthentifiableEntity logged) {
		return MapParser.unparse(getReadableFields(e, entityClass, logged), e);
	}

	public static <T extends BaseEntity> Map<String, Object> read(Class<T> entityClass, T e) {
		return read(entityClass, e, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T create(Class<T> entityClass, Map<String, Object> e, AuthentifiableEntity logged) {
		return (T) MapParser.parse(getCreatableFields(e, entityClass, logged), e);
	}

	public static <T extends BaseEntity> T create(Class<T> entityClass, Map<String, Object> e) {
		return create(entityClass, e, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> T update(Class<T> entityClass, Map<String, Object> e, AuthentifiableEntity logged) {
		return (T) MapParser.parse(getWritableFields(e, entityClass, logged), e);
	}

	public static <T extends BaseEntity> T update(Class<T> entityClass, Map<String, Object> e) {
		return update(entityClass, e, null);
	}


}
