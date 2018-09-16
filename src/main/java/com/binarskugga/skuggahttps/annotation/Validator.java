package com.binarskugga.skuggahttps.annotation;

import com.binarskugga.skuggahttps.http.api.filter.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Validator {
	String name() default "";
	Class<? extends ParameterValidator> value();
}
