package pl.starterkit.stocks.persistence.memory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.starterkit.stocks.model.interfaces.Identifable;

public class AbstractDaoTestImpl {

	private AbstractDAOImpl<String, PersistableObject> dao;
	
	@Before
	public void before() {
		dao = new AbstractDAOImpl<>();
	}
	
	@Test
	public void testAbstractDAOPersistsObjects() {
		PersistableObject object = new PersistableObject("key", "value");
		PersistableObject createdObject = dao.create(object);
		Assert.assertEquals(object, createdObject);
	}
	
	
	@Data
	@AllArgsConstructor
	static class PersistableObject implements Identifable<String> {
		private String id;
		private String value;
	}
}
