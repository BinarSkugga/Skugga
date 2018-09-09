package com.binarskugga.skuggahttps.data;

import lombok.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public abstract class DataRepository<Q, I extends Serializable, T> {

	@Getter private Class<T> clazz;

	protected DataRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	public abstract T id(I id);
	public abstract T id(String id);
	public abstract T single(Function<Q, T> filter);
	public abstract List<T> list(Function<Q, List<T>> filter);

	public abstract T create(T entity);
	public abstract List<T> createAll(Iterable<T> entities);

	public abstract T update(T entity);
	public abstract List<T> updateAll(Iterable<T> entities);

	public abstract boolean delete(T entity);
	public abstract boolean deleteAll(Iterable<T> entities);

}
