package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.types.dbtranslator.v1.StatusType;

/**
 * @author lteixeira
 *
 */
public class StoreOrderDaoTest {
	
	// Daos
	private static StoreOrderDao dao;
	private static SupplierDao sdao;
	private static ProductDao pdao;
	private static StoreDao storedao;

	/**
	 * Set up daos with the h2db settings. Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new StoreOrderDao();
		sdao = new SupplierDao();
		pdao = new ProductDao();
		storedao = new StoreDao();

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
		dao.persist(order);
	}

	/**
	 * Save an entity with all its attributtes.
	 * @throws Exception 
	 */
	@Test
	public void testPersist() throws Exception {
		Long id = saveEntity();
		StoreOrder order = dao.findById(id);
		assertEquals(order.getStatus(), StatusType.PROCESSING.toString());
	}

	/** find an entity
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception {
		Long id = saveEntity();
		StoreOrder order = dao.findById(id);
		assertNull(order.getUpdateDate());
		assertNotNull(order.getInsertDate());
	}

	/**Delete an entity
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception {
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}

	/**Save a entity and return a id
	 * @return
	 * @throws Exception 
	 */
	public Long saveEntity() throws Exception {
		StoreOrder order = new StoreOrder();
		order.setItemStoreOrderList(new ArrayList<ItemStoreOrder>());
		Store store = storedao.findById(1L);
		order.setStore(store);
		order = dao.persist(order);
		return order.getStoreOrderId();
	}
}
