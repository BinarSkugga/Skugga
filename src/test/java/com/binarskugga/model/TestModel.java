package com.binarskugga.model;

import com.binarskugga.impl.*;
import com.binarskugga.skuggahttps.api.AuthentifiableEntity;
import com.binarskugga.skuggahttps.api.Role;
import com.binarskugga.skuggahttps.api.annotation.*;
import com.binarskugga.skuggahttps.api.enums.InclusionMode;
import com.googlecode.objectify.annotation.*;
import lombok.*;
import org.bson.types.*;

@Entity(name = "test") @Authenticator(repository = ObjectifyRepository.class)
@Builder @NoArgsConstructor @AllArgsConstructor
public class TestModel implements AuthentifiableEntity<TestModel, String> {

	@Id
	@Getter @Setter String id = new ObjectId().toHexString();
	@Ignore
	@Getter @Setter Role role;

	@Index
	@Getter @Setter String authentifier;
	@Getter @Setter String passwordHash;

	@Getter @Setter String passwordSalt;
	@Getter @Setter long lastPasswordChange;

}
