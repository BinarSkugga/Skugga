package com.binarskugga.skugga.api;

import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.endpoint.HttpSession;

public interface ExceptionParser extends Parser<Throwable, HttpSession, Object, Endpoint> {
}
