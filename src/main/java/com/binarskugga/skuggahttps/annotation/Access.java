package com.binarskugga.skuggahttps.annotation;

import com.binarskugga.skuggahttps.auth.role.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Access {
	Class<? extends AccessRole>[] value();
}
