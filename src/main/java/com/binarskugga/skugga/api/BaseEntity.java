package com.binarskugga.skugga.api;

import java.io.Serializable;


public interface BaseEntity<I extends Serializable, E extends AuthentifiableEntity<E, I>> {

	I getId();

	default boolean isUnique() {
		return true;
	}

	default void onLoaded(E entity) {
	}

	default void onCreate(E entity) {
	}

	default void onCreated(E entity) {
	}

	default void onUpdate(E entity) {
	}

	default void onUpdated(E entity) {
	}

	default void onDelete(E entity) {
	}

	default void onDeleted(E entity) {
	}

}
