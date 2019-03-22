package com.binarskugga.skugga.api.annotation;

import com.binarskugga.skugga.api.PermissionPredicate;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Repeatable(Permissions.class)
public @interface Permission {

	String value() default "";
	String[] roles() default { "*" };
	Class<? extends PermissionPredicate>[] conditions() default {};

}
