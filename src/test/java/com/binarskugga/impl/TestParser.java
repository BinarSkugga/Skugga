package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.FieldParser;
import com.binarskugga.skuggahttps.api.annotation.IgnoreParser;

import java.lang.reflect.Field;

@IgnoreParser
public class TestParser implements FieldParser<Object, Object> {

	@Override
	public Object parse(Field context, Object object) {
		return null;
	}

}
