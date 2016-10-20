package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Supplier;

public class ProductDaoTest {
	//Daos
	private static SupplierDao sdao;
	private static ProductDao dao;
	
	/**Set up daos with the h2db settings.
	 * Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception{
		
		sdao = new SupplierDao();
		dao = new ProductDao();
		
		Supplier supplier = new Supplier();
		supplier.setName("Supplier");
		supplier = sdao.persist(supplier);
		
		Product product = new Product();
		product.setName("Banana");
		product.setProductSupplierId(1L);
		product.setSupplier(supplier);
		product = dao.persist(product);
	}
	
	public Long saveEntity() throws Exception{
		Product product = new Product();
		product.setName("Heineken");
		product.setProductSupplierId(2L);
		product.setSupplier(sdao.findById(1L));
		product = dao.persist(product);
		return product.getInternalId();
	}
	
	/**Save a entity with all its attributtes.
	 * @throws Exception 
	 */
	@Test
	public void testPersist() throws Exception {
		Long id = saveEntity();
		Product product = dao.findById(id);
		assertTrue(product.getInternalId()>0);
	}
	
	/**Find a product that was just saved.
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception{
		Long id = saveEntity();
		Product product = dao.findById(id);
		assertEquals(product.getInternalId(), id);
	}
	
	/**Delete a Entity.
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception{
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
}
