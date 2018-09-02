package com.binarskugga.skuggahttps.auth;

import com.binarskugga.skuggahttps.auth.role.*;

import java.io.*;

public interface GenericUser<I extends Serializable> {

	I getId();

	String getPasswordHash();
	Class<? extends AccessRole> getAccessRole();

}
