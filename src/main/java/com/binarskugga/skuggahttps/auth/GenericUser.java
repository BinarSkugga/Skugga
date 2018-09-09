package com.binarskugga.skuggahttps.auth;

import com.binarskugga.skuggahttps.auth.role.*;

import java.io.*;

public interface GenericUser<I extends Serializable> extends Identifiable<I> {

	String getPasswordHash();
	Class<? extends AccessRole> getAccessRole();

}
