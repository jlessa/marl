package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.Store;

public class StoreDaoTest {
	// Dao
	private static StoreDao dao;

	/**
	 * Set up daos with the h2db settings. Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new StoreDao();
		
		Store store = new Store();
		store.setName("Bar");
		store = dao.persist(store);
	}

	/**
	 * Persist an entity and check if its id is greater than 1.
	 * Another store has been saved before
	 * @throws Exception 
	 */
	@Test
	public void testPersistEntry() throws Exception {
		Long id = saveEntity();
		assertTrue(id > 1);
	}

	/**
	 * Find the entry saved.
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception {
		Long id = saveEntity();
		Store store = dao.findById(id);
		assertEquals(store.getStoreId(), id);
	}

	/**
	 * Try to remove an Entity
	 * @throws Exception 
	 */
	@Test
	public void testRemove() throws Exception {
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
	
	/**Save an entity in DB
	 * @return
	 * @throws Exception 
	 */
	private Long saveEntity() throws Exception {
		Store store = new Store();
		store.setName("Boteco");
		store = dao.persist(store);
		return store.getStoreId();
	}
}
