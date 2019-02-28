package com.binarskugga.skugga.api;

import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.parse.BodyInformation;

public interface BodyParser<T> extends Parser<Object, BodyInformation, T, Endpoint> {
}
