package com.binarskugga.skuggahttps.server.model;

import lombok.*;
import org.bson.types.*;
import org.mongodb.morphia.annotations.*;

@Entity("test")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Test {

	@Id
	@Getter @Setter private ObjectId id;

	@Getter @Setter private String value;

}
