package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.impl.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Type;

@AllArgsConstructor
public class BodyInformation {

	@Getter private Type[] innerTypes;
	@Getter private Class collectionClass;
	@Getter private HttpSession session;

}
