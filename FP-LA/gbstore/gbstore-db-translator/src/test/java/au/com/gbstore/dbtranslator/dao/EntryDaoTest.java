package au.com.gbstore.dbtranslator.dao;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.Entry;
import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.dbtranslator.model.Supplier;

public class EntryDaoTest {
	//Declaring Daos
	private static EntryDao dao;
	private static SupplierDao sdao;
	private static ProductDao pdao;
	private static StoreDao stdao;
	private static StoreOrderDao sodao;
	
	/**Set up daos with the h2db settings.
	 * Save supplier, store and product beforehand
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUp() throws Exception{
		dao = new EntryDao();
		sdao = new SupplierDao();
		pdao = new ProductDao();
		stdao = new StoreDao();
		sodao = new StoreOrderDao();
		
		Store boteco = new Store();
		boteco.setName("Boteco");
		try {
			stdao.persist(boteco);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Store devassa = new Store();
		devassa.setName("Devassa");
		stdao.persist(devassa);
		
		Store bar = new Store();
		bar.setName("Bar");
		stdao.persist(bar);
		
		Supplier suppliera = new Supplier();
		suppliera.setName("SupplierA");
		suppliera = sdao.persist(suppliera);
		
		Supplier supplierb = new Supplier();
		supplierb.setName("SupplierB");
		supplierb = sdao.persist(supplierb);
		
		Supplier supplierc = new Supplier();
		supplierc.setName("SupplierC");
		supplierc = sdao.persist(supplierc);
		
		Product budweiser = new Product();
		budweiser.setName("Budweiser");
		budweiser.setProductSupplierId(1L);
		budweiser.setSupplier(suppliera);
		budweiser = pdao.persist(budweiser);
		
		Product amstel = new Product();
		amstel.setName("Amstel");
		amstel.setProductSupplierId(2L);
		amstel.setSupplier(supplierb);
		amstel = pdao.persist(amstel);
		
		Product heineken = new Product();
		heineken.setName("Heineken");
		heineken.setProductSupplierId(3L);
		heineken.setSupplier(supplierc);
		heineken = pdao.persist(heineken);
		
		StoreOrder sorder = new StoreOrder();
		sorder.setStore(boteco);
		
		ItemStoreOrder item1 = new ItemStoreOrder();
		item1.setQuantity(BigInteger.valueOf(100));
		Product product = budweiser;
		item1.setProduct(product);
		item1.setStoreOrder(sorder);
		
		
		ItemStoreOrder item2 = new ItemStoreOrder();
		item2.setQuantity(BigInteger.valueOf(200));
		product = amstel;
		item2.setProduct(product);
		item2.setStoreOrder(sorder);

		ItemStoreOrder item3 = new ItemStoreOrder();
		item3.setQuantity(BigInteger.valueOf(300));
		product = heineken;
		item3.setProduct(product);
		item3.setStoreOrder(sorder);
		
		sorder.getItemStoreOrderList().add(item1);
		sorder.getItemStoreOrderList().add(item2);
		sorder.getItemStoreOrderList().add(item3);
		sorder = sodao.persist(sorder);
		
		StoreOrder sorder2 = new StoreOrder();
		sorder2.setStore(bar);
		item1.setStoreOrder(sorder2);
		item2.setStoreOrder(sorder2);
		sorder.getItemStoreOrderList().add(item1);
		sorder.getItemStoreOrderList().add(item2);
		sorder2 = sodao.persist(sorder2);
		
	}
	/**Save a new entry.
	 * @return
	 * @throws Exception 
	 */
	public Long saveEntity() throws Exception{
		Entry entry = new Entry();
		List<StoreOrder> list = new ArrayList<StoreOrder>();
		List<ItemStoreOrder> listItem = new ArrayList<ItemStoreOrder>();
		ItemStoreOrder item = new ItemStoreOrder();
		
		entry.setInsertDate(new Date());
		
		StoreOrder order = new StoreOrder();
		order.setInsertDate(new Date());
		
		Store store = stdao.findById(1L);
		order.setStore(store);
		
		Product product =  pdao.findById(1L);
		item.setProduct(product);
		
		listItem.add(item);
		order.setItemStoreOrderList(listItem);
		item.setStoreOrder(order);
		list.add(order);
		entry.setStoreOrderList(list);
		
		entry = dao.persist(entry);
		return entry.getEntryId();
	}
	
	/**Verify persist method.
	 * It must persist a new entity and generate a new id
	 * It must has a StoreOrder
	 * @throws Exception 
	 */
	@Test
	public void testPersistEntry() throws Exception {
		Long id = saveEntity();
		Long otherId = saveEntity();
		assertNotEquals(id, otherId);
		Entry entry = dao.findById(id);
		assertNotNull(entry.getStoreOrderList().get(0).getItemStoreOrderList().get(0).getStoreOrder());
	}
	/**Find the entry saved on saveEntry method.
	 * @throws Exception 
	 */
	@Test
	public void testFindById() throws Exception{
		Long id = saveEntity();
		Entry newEntry = dao.findById(id);
		assertNotNull(newEntry);
	}
	
	/**Try to find a 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testFindANonEntity() throws Exception{
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
		
	}
	/**Try to remove an Entity
	 * @throws Exception 
	 */
	@Test
	public void testRemove() throws Exception{
		Long id = saveEntity();
		dao.remove(id);
		assertNull(dao.findById(id));
	}
}
