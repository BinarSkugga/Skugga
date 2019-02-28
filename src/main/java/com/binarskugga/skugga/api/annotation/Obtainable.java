package com.binarskugga.skugga.api.annotation;

import com.binarskugga.skugga.api.enums.InclusionMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Obtainable {

	// Defaults to the connected value of the endpoint.
	String[] roles() default {"*"};

	InclusionMode inclusion() default InclusionMode.INCLUDE;

}
