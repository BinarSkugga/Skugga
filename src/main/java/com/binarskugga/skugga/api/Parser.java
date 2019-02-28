package com.binarskugga.skugga.api;

public interface Parser<R, T, U, P> {

	R parse(T context, U object) throws Exception;

	default U unparse(T context, R object) throws Exception {
		return null;
	}

	default boolean predicate(P c) {
		return true;
	}

}
