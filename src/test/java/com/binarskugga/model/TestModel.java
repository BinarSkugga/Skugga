package com.binarskugga.model;

import com.binarskugga.impl.ObjectifyRepository;
import com.binarskugga.skuggahttps.api.AuthentifiableEntity;
import com.binarskugga.skuggahttps.api.Role;
import com.binarskugga.skuggahttps.api.annotation.Authenticator;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import lombok.*;
import org.bson.types.ObjectId;

@Entity(name = "test")
@Authenticator(repository = ObjectifyRepository.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestModel implements AuthentifiableEntity<TestModel, String> {

	@Id
	@Getter
	@Setter
	String id = new ObjectId().toHexString();
	@Ignore
	@Getter
	@Setter
	Role role;

	@Index
	@Getter
	@Setter
	String authentifier;
	@Getter
	@Setter
	String passwordHash;

	@Getter
	@Setter
	String passwordSalt;
	@Getter
	@Setter
	long lastPasswordChange;

}
