package com.binarskugga.skuggahttps.http.api.filter;

import com.binarskugga.skuggahttps.http.*;

public interface AbstractFilter {
	boolean apply(HttpSession session);
}
