package com.binarskugga.skuggahttps.api.annotation;

import com.binarskugga.skuggahttps.api.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Authenticator {

	Class<? extends DataRepository> repository();

}
