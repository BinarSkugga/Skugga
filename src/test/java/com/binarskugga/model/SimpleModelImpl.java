package com.binarskugga.model;

import com.binarskugga.skugga.api.*;
import com.googlecode.objectify.annotation.*;
import lombok.*;
import org.bson.types.*;

@Entity(name = "simple_model")
@Builder @NoArgsConstructor @AllArgsConstructor
public class SimpleModelImpl implements BaseEntity<String> {

	@Id
	@Getter @Setter private String id = new ObjectId().toHexString();

	@Index
	@Getter @Setter private String name;

	@Getter @Setter private int age;

}
