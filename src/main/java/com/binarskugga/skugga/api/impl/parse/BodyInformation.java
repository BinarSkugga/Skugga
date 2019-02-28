package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.skugga.api.impl.endpoint.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Type;

@AllArgsConstructor
public class BodyInformation {

	@Getter private Type[] innerTypes;
	@Getter private Class collectionClass;
	@Getter private HttpSession session;

}
