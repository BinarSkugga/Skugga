package com.binarskugga.skuggahttps.api;

public interface MappingPredicate<F, T, U> {

	default boolean applyToMap(F value, T obj) { return true; }
	default boolean applyToEntity(F value, U obj) { return true; }

}
