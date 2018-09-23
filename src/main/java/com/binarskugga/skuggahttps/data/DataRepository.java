package com.binarskugga.skuggahttps.data;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.auth.*;
import com.binarskugga.skuggahttps.utils.*;
import com.google.common.cache.*;
import com.google.common.hash.*;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public abstract class DataRepository<Q, I extends Serializable, T extends Identifiable<I>> {

	@Getter private Class<T> clazz;

	private static PropertiesConfiguration configuration;
	private static Cache<Serializable, Identifiable> idCache;
	private static BloomFilter<Serializable> idCacheFilter;

	static {
		configuration = HttpConfigProvider.get();
		idCache = CacheBuilder.newBuilder().maximumSize(configuration.getInt("server.cache.data.size").orElse(1000))
				.expireAfterWrite(20, TimeUnit.MINUTES).build();
		idCacheFilter = BloomFilter.create(new SerializableFunnel(), configuration.getInt("server.cache.data.size").orElse(1000));
	}

	protected DataRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T id(I id) {
		T item = getFromCache(id);
		if(item != null)
			return item;

		item = this.byId(id);
		if(item != null) this.cacheEntity(item);
		return item;
	}

	public T update(T entity) {
		this.invalidateEntity(entity);
		return this.doUpdate(entity);
	}

	protected abstract T byId(I id);
	public abstract T id(String id);
	public abstract T single(Function<Q, T> filter);
	public abstract List<T> list(Function<Q, List<T>> filter);

	public abstract T create(T entity);
	public abstract List<T> createAll(Iterable<T> entities);

	protected abstract T doUpdate(T entity);
	public abstract List<T> updateAll(Iterable<T> entities);

	public abstract boolean delete(T entity);
	public abstract boolean deleteAll(Iterable<T> entities);

	public T getFromCache(I id) {
		if(idCacheFilter.mightContain(id))
			return (T) idCache.getIfPresent(id);
		return null;
	}

	public void cacheEntity(T entity) {
		idCache.put(entity.getId(), entity);
		idCacheFilter.put(entity.getId());
	}

	public void invalidateEntity(T entity) {
		idCache.invalidate(entity.getId());
	}

	public void invalidateEntity(I id) {
		idCache.invalidate(id);
	}

}
