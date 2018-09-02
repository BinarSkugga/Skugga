package com.binarskugga.skuggahttps.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Post {

	String value() default "";
}
