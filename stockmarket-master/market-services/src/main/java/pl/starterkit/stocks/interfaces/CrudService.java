package pl.starterkit.stocks.interfaces;

import java.util.List;

public interface CrudService<T, IDT> {

	public T getById(IDT id);
	public List<T> getAll();
	public T update(T object);
	public void delete(IDT id);
	public T create(T object);
}
