package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.HttpSession;
import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;

public interface ExceptionParser<T> extends Parser<Throwable, HttpSession, T, Endpoint> {}
