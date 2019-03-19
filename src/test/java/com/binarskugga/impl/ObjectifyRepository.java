package com.binarskugga.impl;

import com.binarskugga.skugga.api.AuthentifiableEntity;
import com.binarskugga.skugga.api.BaseEntity;
import com.binarskugga.skugga.api.DataRepository;
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
	private AuthentifiableEntity<?, String> authenticated;

	public ObjectifyRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	public ObjectifyRepository(Class<T> clazz, AuthentifiableEntity<?, String> authenticated) {
		this.clazz = clazz;
		this.authenticated = authenticated;
	}

	@Override
	public AuthentifiableEntity<?, String> authenticatedEntity() {
		return authenticated;
	}

	@Override
	public String createId(T entity) {
		return new ObjectId().toHexString();
	}

	@Override
	public T load(String id) {
		T entity = ofy().load().key(Key.create(this.clazz, id)).now();
		if (entity != null) entity.onLoaded(this.authenticated);
		return entity;
	}

	@Override
	public T load(String column, Object value) {
		T entity = ofy().load().type(this.clazz).filter(column, value).first().now();
		if (entity != null) entity.onLoaded(this.authenticated);
		return entity;
	}

	@Override
	public T load(Function<LoadType<T>, T> filter) {
		T entity = filter.apply(ofy().load().type(this.clazz));
		if (entity != null) entity.onLoaded(this.authenticated);
		return entity;
	}

	@Override
	public List<T> loadList(Function<LoadType<T>, List<T>> filter) {
		List<T> entities = filter.apply(ofy().load().type(this.clazz));
		if (entities.size() > 0) entities.forEach(e -> e.onLoaded(this.authenticated));
		return entities;
	}

	@Override
	public List<T> loadIdsList(List<String> ids) {
		List<Key<T>> keys = ids.stream().map(i -> Key.create(this.clazz, i)).collect(Collectors.toList());
		List<T> entities = (List<T>) ofy().load().keys(keys).values();
		if (entities.size() > 0) entities.forEach(e -> e.onLoaded(this.authenticated));
		return entities;
	}

	@Override
	public List<T> loadAll() {
		return this.loadList(QueryExecute::list);
	}

	@Override
	public T save(boolean update, T entity) {
		if (update) entity.onUpdate(this.authenticated);
		else entity.onCreate(this.authenticated);

		ofy().save().entity(entity).now();

		if (update) entity.onUpdated(this.authenticated);
		else entity.onCreated(this.authenticated);
		return entity;
	}

	@Override
	public List<T> saveList(boolean update, List<T> entities) {
		if (update) entities.forEach(e -> e.onUpdate(this.authenticated));
		else entities.forEach(e -> e.onCreate(this.authenticated));

		ofy().save().entities(entities).now();

		if (update) entities.forEach(e -> e.onUpdated(this.authenticated));
		else entities.forEach(e -> e.onCreated(this.authenticated));
		return entities;
	}

	@Override
	public String delete(T entity) {
		String id = entity.getId();
		entity.onDelete(this.authenticated);
		ofy().delete().entity(entity).now();
		entity.onDeleted(this.authenticated);
		return id;
	}

	@Override
	public List<String> deleteList(List<T> entities) {
		List<String> ids = entities.stream().map(BaseEntity::getId).collect(Collectors.toList());
		entities.forEach(e -> e.onDelete(this.authenticated));
		ofy().delete().entities(entities).now();
		entities.forEach(e -> e.onDeleted(this.authenticated));
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
