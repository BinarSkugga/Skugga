package com.binarskugga.skuggahttps.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public interface DataRepository<I extends Serializable, C, T extends BaseEntity<I>, Q> {

	I createId(T entity);

	T load(I id);
	T load(C column, Object value);
	T load(Function<Q, T> filter);

	List<T> loadList(Function<Q, List<T>> filter);
	List<T> loadIdsList(List<I> ids);
	default List<T> loadIdsList(I... ids) {
		return this.loadIdsList(Arrays.asList(ids));
	}
	List<T> loadAll();

	T save(boolean update, T entity);
	default T save(T entity) {
		return this.save(false, entity);
	}
	default T update(T entity) {
		return this.save(true, entity);
	}

	List<T> saveList(boolean update, List<T> entities);
	default List<T> saveList(List<T> entities) {
		return this.saveList(false, entities);
	}
	default List<T> saveList(boolean update, T... entities) {
		return this.saveList(update, Arrays.asList(entities));
	}
	default List<T> saveList(T... entities) {
		return this.saveList(false, entities);
	}

	default List<T> updateList(List<T> entities) {
		return this.saveList(true, entities);
	}
	default List<T> updateList(T... entities) {
		return this.saveList(true, Arrays.asList(entities));
	}

	I delete(T entity);
	List<I> deleteList(List<T> entities);
	List<I> deleteIdsList(List<I> ids);
	default List<I> deleteIdsList(I... ids) {
		return deleteIdsList(Arrays.asList(ids));
	}
	default List<I> deleteList(T... entities) {
		return this.deleteList(Arrays.asList(entities));
	}
	void truncate();

}
