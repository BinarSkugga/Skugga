package com.binarskugga;

import com.binarskugga.skuggahttps.api.AuthentifiableEntity;
import com.binarskugga.skuggahttps.api.Role;
import com.binarskugga.skuggahttps.api.annotation.*;
import com.binarskugga.skuggahttps.api.enums.InclusionMode;
import lombok.*;

@Creatable
@Builder @NoArgsConstructor @AllArgsConstructor
public class TestModel implements AuthentifiableEntity<TestModel, Integer> {

	@CreateField(inclusion = InclusionMode.EXCLUDE)
	@Getter @Setter Integer id;
	@CreateField(inclusion = InclusionMode.EXCLUDE)
	@Getter @Setter Role role;

	@Getter @Setter String authentifier;
	@MappingOptions(name = "password_hash")
	@Getter @Setter String passwordHash;

	@CreateField(inclusion = InclusionMode.EXCLUDE)
	@Getter @Setter String passwordSalt;
	@CreateField(inclusion = InclusionMode.EXCLUDE)
	@Getter @Setter long lastPasswordChange;

}
