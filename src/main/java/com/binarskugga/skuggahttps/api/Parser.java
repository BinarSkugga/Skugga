package com.binarskugga.skuggahttps.api;

public interface Parser<R, T, U, P> {

	R parse(T context, U object);

	default U unparse(T context, R object) {
		return null;
	}

	default boolean predicate(P c) {
		return true;
	}

}
