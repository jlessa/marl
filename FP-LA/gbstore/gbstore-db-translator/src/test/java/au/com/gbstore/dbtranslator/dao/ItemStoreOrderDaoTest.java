package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.dbtranslator.model.Supplier;

public class ItemStoreOrderDaoTest {
	//Daos
	private static ItemStoreOrderDao dao;
	private static SupplierDao sdao;
	private static ProductDao pdao;
	private static StoreDao storedao;
	private static StoreOrderDao sodao;
	
	/**Set up daos with the h2db settings.
	 * Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception{
		dao = new ItemStoreOrderDao();
		sdao = new SupplierDao();
		pdao = new ProductDao();
		storedao = new StoreDao();
		sodao = new StoreOrderDao();
		
		Supplier supplier = new Supplier();
		supplier.setName("Supplier");
		supplier = sdao.persist(supplier);
		
		
		Store store = new Store();
		store.setName("Store");
		store = storedao.persist(store);
		
		Product product = new Product();
		product.setProductSupplierId(1L);
		product.setSupplier(supplier);
		product = pdao.persist(product);
		
		StoreOrder order = new StoreOrder();
		order.setInsertDate(new Date());
		order.setItemStoreOrderList(new ArrayList<ItemStoreOrder>());
		order.setStore(store);
		sodao.persist(order);
	}
	
	/**Save an Entity in DB and return its ID.
	 * @return
	 * @throws Exception 
	 */
	public Long saveEntity() throws Exception{
		ItemStoreOrder item = new ItemStoreOrder();
		Product product = pdao.findById(1L);
		
		item.setProduct(product);
		item.setQuantity(BigInteger.TEN);
		item.setStoreOrder(sodao.findById(1L));
		
		item = dao.persist(item);
		return item.getItemStoreOrderId();
	}
	
	/**Save a entity with all its attributtes.
	 * @throws Exception 
	 */
	@Test
	public void testPersist() throws Exception {
		Long id = saveEntity();
		ItemStoreOrder item = dao.findById(id);
		assertNotNull(item.getItemStoreOrderId());
	}
	/**Save and entry and seach for it.
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception{
		Long id = saveEntity();
		ItemStoreOrder item = dao.findById(id);
		assertEquals(item.getQuantity(), BigInteger.TEN);
	}
	/**Try to remove an Entity.
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception{
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
}
