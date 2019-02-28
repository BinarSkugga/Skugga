package com.binarskugga.skugga.api;

import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;

public interface ExceptionParser<T> extends Parser<Throwable, HttpSession, T, Endpoint> {
}
