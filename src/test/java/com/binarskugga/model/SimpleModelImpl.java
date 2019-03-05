package com.binarskugga.model;

import com.binarskugga.model.embedded.EmbeddedField;
import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.annotation.Creatable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.*;
import org.bson.types.ObjectId;

@Entity(name = "simple_model") @Creatable
@Builder @NoArgsConstructor @AllArgsConstructor
public class SimpleModelImpl implements BaseEntity<String> {

	@Id
	@Getter @Setter private String id = new ObjectId().toHexString();

	@Index
	@Getter @Setter private String name;

	@Getter @Setter private int age;
	@Getter @Setter private byte[] test;
	@Getter @Setter private EmbeddedField em;

}
