package com.binarskugga.skuggahttps.api;

import java.io.Serializable;


public interface BaseEntity<I extends Serializable> {

	I getId();

	default void onLoaded() {}
	default void onCreate() {}
	default void onCreated() {}
	default void onUpdate() {}
	default void onUpdated() {}
	default void onDelete() {}
	default void onDeleted() {}

}
