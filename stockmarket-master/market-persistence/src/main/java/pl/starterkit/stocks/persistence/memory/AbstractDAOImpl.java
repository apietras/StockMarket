package pl.starterkit.stocks.persistence.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.starterkit.stocks.model.interfaces.Identifable;
import pl.starterkit.stocks.persistence.AbstractDAO;

public class AbstractDAOImpl<K, T extends Identifable<K>> implements AbstractDAO<K, T> {

	protected Map<K, T> objectsMap = new HashMap<K, T>();
	
	public T getById(K id) {
		return objectsMap.get(id);
	}

	public List<T> getAll() {
		return new ArrayList<T>(objectsMap.values());
	}

	public T create(T object) {
		objectsMap.put(object.getId(), object);
		return object;
	}

	public T update(T object) {
		objectsMap.put(object.getId(), object);
		return object;
	}

	public T delete(K id) {
		return objectsMap.remove(id);
	}

}
