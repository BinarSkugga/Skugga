package com.binarskugga.skugga.api;

import com.binarskugga.skugga.api.impl.endpoint.Endpoint;
import com.binarskugga.skugga.api.impl.parse.BodyInformation;

public interface BodyParser extends Parser<Object, BodyInformation, Object, Endpoint> {
}
