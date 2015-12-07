package pl.starterkit.stocks.persistence;

import java.util.List;

public interface AbstractDAO <K, T> {
	public T getById(K id);
	public List<T> getAll();
	public T create(T object);
	public T update(T object);
	public T delete(K id);
}
