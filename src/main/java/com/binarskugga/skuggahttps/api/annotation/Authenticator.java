package com.binarskugga.skuggahttps.api.annotation;

import com.binarskugga.skuggahttps.api.DataRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Authenticator {

	Class<? extends DataRepository> value();

}
