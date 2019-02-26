package com.binarskugga.skuggahttps.api;

import com.binarskugga.skuggahttps.api.impl.endpoint.Endpoint;
import com.binarskugga.skuggahttps.api.impl.parse.BodyInformation;

public interface BodyParser<T> extends Parser<Object, BodyInformation, T, Endpoint> {
}
