package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.Supplier;

/**
 * @author lteixeira
 *
 */
public class SupplierDaoTest {
	private static SupplierDao dao;
	/**Set up dao with the h2db settings.
	 */
	@BeforeClass
	public static void setUp(){
		dao = new SupplierDao();
	}
	
	/**Save a entity with all its attributtes.
	 * @throws Exception 
	 */
	@Test
	public void testPersist() throws Exception {
		Long id = saveEntity();
		Supplier supplier = dao.findById(id);
		assertEquals(supplier.getName(), "SupplierA");
	}
	
	/**Find a entity
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception{
		Long id = saveEntity();
		assertNotNull(dao.findById(id));
	}
	
	/**Delete an entity
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception{
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
	
	/**Save an entity.
	 * return its ID
	 * @throws Exception 
	 */
	public Long saveEntity() throws Exception{
		Supplier supplier = new Supplier();
		supplier.setName("SupplierA");
		supplier = dao.persist(supplier);
		return supplier.getSupplierId();
	}
}
