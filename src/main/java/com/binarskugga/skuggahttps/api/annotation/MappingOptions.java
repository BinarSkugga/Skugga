package com.binarskugga.skuggahttps.api.annotation;

import com.binarskugga.skuggahttps.api.*;
import com.binarskugga.skuggahttps.api.impl.map.field.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingOptions {

	Class<? extends FieldMapper> mapper() default DefaultOptionsMapper.class;
	String name() default "";

	Class<? extends MappingPredicate>[] predicates() default {};

}
