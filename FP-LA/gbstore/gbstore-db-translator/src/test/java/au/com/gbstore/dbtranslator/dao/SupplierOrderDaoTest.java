package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.ItemSupplierOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.dbtranslator.model.SupplierOrder;
import au.com.gbstore.types.dbtranslator.v1.StatusType;

public class SupplierOrderDaoTest {

	// Daos
	private static SupplierOrderDao dao;
	private static SupplierDao sdao;
	private static ProductDao pdao;
	private static ItemSupplierOrderDao idao;

	/**
	 * Set up daos with the h2db settings. Save supplier, store and product
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new SupplierOrderDao();
		sdao = new SupplierDao();
		pdao = new ProductDao();
		idao = new ItemSupplierOrderDao();
		
		Supplier supplier = new Supplier();
		supplier.setName("Supplier");
		supplier = sdao.persist(supplier);

		Product product = new Product();
		product.setProductSupplierId(1L);
		product.setSupplier(supplier);
		product = pdao.persist(product);
	}

	/**
	 * Save a entity with all its attributtes.
	 * @throws Exception 
	 */
	@Test
	public void testPersistNewSupplierOrder() throws Exception {
		Long id = saveEntity();
		SupplierOrder order = dao.findById(id);
		assertNull(order.getUpdateDate());
	}

	@Test
	public void testCheckStatus() throws Exception {
		Long id = saveEntity();
		SupplierOrder order = dao.findById(id);
		assertTrue(order.getStatus().contains(StatusType.PROCESSING.toString()));
	}

	/**
	 * Update Supplier Order with order number, new status and updateDate
	 * @throws Exception 
	 */
	@Test
	public void testSaveOrderNumber() throws Exception {
		Long id = saveEntity();
		SupplierOrder order = dao.findById(id);
		order.setUpdateDate(new Date());
		order.setOrderNumber("123");
		order.setStatus(StatusType.ORDERED.toString());
		order = dao.persist(order);
		assertEquals(order.getSupplierOrderId(), id);
	}

	/**
	 * Find an entity
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testFindById() throws Exception {
		Long id = saveEntity();
		assertNotNull(dao.findById(id));
	}

	/**
	 * Save an entity
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Long saveEntity() throws Exception {
		ItemSupplierOrder item = new ItemSupplierOrder();
		item.setProduct(pdao.findById(1L));
		item.setQuantity(BigInteger.TEN);
		
		
		SupplierOrder order = new SupplierOrder();
		order.setInsertDate(new Date());
		order.setSupplier(sdao.findById(1L));
		
		List<ItemSupplierOrder> list = new ArrayList<ItemSupplierOrder>();
		list.add(item);
		order.setItemSupplierOrderList(list);
		order = dao.persist(order);
		
		item.setSupplierOrder(order);
		item = idao.persist(item);
		return order.getSupplierOrderId();
	}
}
