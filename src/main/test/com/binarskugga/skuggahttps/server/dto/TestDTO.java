package com.binarskugga.skuggahttps.server.dto;

import com.binarskugga.skuggahttps.validation.*;
import lombok.*;
import org.bson.types.*;

import java.util.*;

public class TestDTO implements ForeignInput {

	@Getter @Setter private ObjectId id;

	@Getter @Setter private String value;

	@Override
	public Set<ValidationError> validate() {
		return new Validator()
				.string("value", this.value)
					.length(3, 8).next()
				.done();
	}

}
