package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.HttpSession;

public interface ExceptionParser {

	String toString(HttpSession session, Throwable throwable);

}
