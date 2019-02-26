package com.binarskugga.impl;

import com.binarskugga.skuggahttps.api.BaseEntity;
import com.binarskugga.skuggahttps.api.DataRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryExecute;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ObjectifyRepository<T extends BaseEntity<String>> implements DataRepository<String, String, T, LoadType<T>> {

	private Class<T> clazz;

	public ObjectifyRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String createId(T entity) {
		return new ObjectId().toHexString();
	}

	@Override
	public T load(String id) {
		T entity = ofy().load().key(Key.create(this.clazz, id)).now();
		if (entity != null) entity.onLoaded();
		return entity;
	}

	@Override
	public T load(String column, Object value) {
		T entity = ofy().load().type(this.clazz).filter(column, value).first().now();
		if (entity != null) entity.onLoaded();
		return entity;
	}

	@Override
	public T load(Function<LoadType<T>, T> filter) {
		T entity = filter.apply(ofy().load().type(this.clazz));
		if (entity != null) entity.onLoaded();
		return entity;
	}

	@Override
	public List<T> loadList(Function<LoadType<T>, List<T>> filter) {
		List<T> entities = filter.apply(ofy().load().type(this.clazz));
		if (entities.size() > 0) entities.forEach(BaseEntity::onLoaded);
		return entities;
	}

	@Override
	public List<T> loadIdsList(List<String> ids) {
		List<Key<T>> keys = ids.stream().map(i -> Key.create(this.clazz, i)).collect(Collectors.toList());
		List<T> entities = (List<T>) ofy().load().keys(keys).values();
		if (entities.size() > 0) entities.forEach(BaseEntity::onLoaded);
		return entities;
	}

	@Override
	public List<T> loadAll() {
		return this.loadList(QueryExecute::list);
	}

	@Override
	public T save(boolean update, T entity) {
		if (update) entity.onUpdate();
		else entity.onCreate();

		ofy().save().entity(entity).now();

		if (update) entity.onUpdated();
		else entity.onCreated();
		return entity;
	}

	@Override
	public List<T> saveList(boolean update, List<T> entities) {
		if (update) entities.forEach(BaseEntity::onUpdate);
		else entities.forEach(BaseEntity::onCreate);

		ofy().save().entities(entities).now();

		if (update) entities.forEach(BaseEntity::onUpdated);
		else entities.forEach(BaseEntity::onCreated);
		return entities;
	}

	@Override
	public String delete(T entity) {
		String id = entity.getId();
		entity.onDelete();
		ofy().delete().entity(entity).now();
		entity.onDeleted();
		return id;
	}

	@Override
	public List<String> deleteList(List<T> entities) {
		List<String> ids = entities.stream().map(BaseEntity::getId).collect(Collectors.toList());
		entities.forEach(BaseEntity::onDelete);
		ofy().delete().entities(entities).now();
		entities.forEach(BaseEntity::onDeleted);
		return ids;
	}

	@Override
	public List<String> deleteIdsList(List<String> ids) {
		List<Key<T>> keys = ids.stream().map(i -> Key.create(this.clazz, i)).collect(Collectors.toList());
		return this.deleteList((List<T>) ofy().load().keys(keys).values());
	}

	@Override
	public void truncate() {
		this.deleteList(this.loadAll());
	}

}
