package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.ItemSupplierOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.dbtranslator.model.SupplierOrder;

public class ItemSupplierOrderDaoTest {

	// Daos
	private static ItemSupplierOrderDao dao;
	private static SupplierDao sdao;
	private static ProductDao pdao;
	private static SupplierOrderDao sodao;

	/**
	 * Set up daos with the h2db settings. Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception {

		dao = new ItemSupplierOrderDao();
		sdao = new SupplierDao();
		pdao = new ProductDao();
		sodao = new SupplierOrderDao();

		Supplier supplier = new Supplier();
		supplier.setName("Supplier");
		supplier = sdao.persist(supplier);

		Product product = new Product();
		product.setProductSupplierId(1L);
		product.setSupplier(supplier);
		product = pdao.persist(product);

		SupplierOrder order = new SupplierOrder();
		order.setInsertDate(new Date());
		order.setSupplier(supplier);
		order.setItemSupplierOrderList(new ArrayList<ItemSupplierOrder>());
		sodao.persist(order);
	}

	/**
	 * Save a entity with all its attributtes.
	 * @throws Exception 
	 */
	public Long saveEntity() throws Exception {
		ItemSupplierOrder order = new ItemSupplierOrder();

		Product product = pdao.findById(1L);
		order.setProduct(product);

		order.setQuantity(BigInteger.valueOf(100L));
		order.setSupplierOrder(new SupplierOrder());
		order = dao.persist(order);
		return order.getItemSupplierOrderId();
	}

	/**
	 * Persist an Entry and search it on DB.
	 * @throws Exception 
	 */
	@Test
	public void testPersist() throws Exception {
		Long id = saveEntity();
		assertNotNull(dao.findById(id));
	}

	/**
	 * Find the entry saved on setup method.
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception {
		Long id = saveEntity();
		ItemSupplierOrder item = dao.findById(id);
		assertEquals(item.getQuantity(), BigInteger.valueOf(100L));
	}

	/**
	 * Try to remove an Entity
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception {
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
}
