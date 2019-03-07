package com.binarskugga.skugga.api.impl.parse.parameter;

import com.binarskugga.skugga.api.ParameterParser;
import com.binarskugga.skugga.api.exception.CannotMapFieldException;
import org.bson.types.ObjectId;

import java.lang.reflect.Parameter;

public class ObjectIdParser implements ParameterParser<ObjectId> {

	@Override
	public ObjectId parse(Parameter parameter, String argument) {
		if(parameter.getType().equals(ObjectId.class)) {
			try {
				return new ObjectId(argument);
			} catch (Exception ignored) {}
		}

		throw new CannotMapFieldException();
	}

	@Override public String unparse(Parameter context, ObjectId object) throws Exception {
		return object.toHexString();
	}

	@Override
	public boolean predicate(Parameter c) {
		return ObjectId.class.equals(c.getType());
	}

}
