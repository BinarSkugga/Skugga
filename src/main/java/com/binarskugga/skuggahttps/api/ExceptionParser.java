package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.api.impl.endpoint.HttpSession;

public interface ExceptionParser<T> extends Parser<Throwable, HttpSession, T, Endpoint> {}
