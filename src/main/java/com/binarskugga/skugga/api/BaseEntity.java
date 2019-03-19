package com.binarskugga.skugga.api;

import java.io.Serializable;


public interface BaseEntity<I extends Serializable> {

	I getId();

	default boolean isUnique() {
		return true;
	}

	default void onLoaded(AuthentifiableEntity<?, I> entity) {
	}

	default void onCreate(AuthentifiableEntity<?, I> entity) {
	}

	default void onCreated(AuthentifiableEntity<?, I> entity) {
	}

	default void onUpdate(AuthentifiableEntity<?, I> entity) {
	}

	default void onUpdated(AuthentifiableEntity<?, I> entity) {
	}

	default void onDelete(AuthentifiableEntity<?, I> entity) {
	}

	default void onDeleted(AuthentifiableEntity<?, I> entity) {
	}

}
