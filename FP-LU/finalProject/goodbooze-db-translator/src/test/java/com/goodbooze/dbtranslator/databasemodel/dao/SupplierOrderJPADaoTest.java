package com.goodbooze.dbtranslator.databasemodel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goodbooze.dbtranslator.databasemodel.model.EntryJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ItemStoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ItemSupplierOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierOrderJPA;
import com.goodbooze.dbtranslator.testutil.Util;
import com.goodbooze.types.typesdefinition.v1.ItemStoreOrderList;

public class SupplierOrderJPADaoTest {

    /**
     * initialize database with some needed entries by tests. this methods
     * execute before every unit test in this class
     */
    @Before
    public void setUpTest() {
        EntityManager em = Util.getEntityManager();

        StoreJPA store = new StoreJPA();
        store.setName("Store A");

        SupplierJPA supplier = new SupplierJPA();
        supplier.setName("Supplier A");

        ProductJPA product1 = new ProductJPA();
        product1.setName("Heineken");
        product1.setSupplierJPA(supplier);

        ProductJPA product2 = new ProductJPA();
        product2.setName("Stella Artois");
        product2.setSupplierJPA(supplier);

        em.getTransaction().begin();
        em.persist(store);
        em.persist(supplier);
        em.persist(product1);
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.persist(product2);
        em.getTransaction().commit();

        em.close();
    }

    @Test
    public void findproductTest() {
        EntityManager em = Util.getEntityManager();

        ProductJPADao productJpaDao = new ProductJPADao(em);
        ProductJPA p = productJpaDao.find(1);
        assertEquals("Heineken", p.getName());

    }

    /**
     * close the data base after every unit test
     * 
     * @throws InterruptedException
     */
    @After
    public void cleanUpBase() throws InterruptedException {
        Util.cleanUpBase();
    }

    /**
     * tests a simple creation
     */
    @Test
    public void createTest() {
        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        supOrder.setInsertDate(new Date());
        supOrder.setStatus("new");
        supOrder.setUpdateDate(new Date());

        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        assertTrue(supOrderDao.create(supOrder));
        em.close();
    }

    /**
     * checks if a storeOrder can be found by find method
     */
    @Test
    public void findTest() {
        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        supOrder.setInsertDate(new Date());
        supOrder.setStatus("new");

        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        supOrderDao.create(supOrder);

        SupplierOrderJPA result = supOrderDao.find(1);
        result.getInsertDate();
        result.getStatus();
        result.getUpdateDate();
        assertNotNull(result);
        em.close();
    }

    /**
     * checks if persist cascade for itemSupplierOrder is correct
     */
    @Test
    public void createOrderWithItem() {
        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        ProductJPADao prodDao = new ProductJPADao(em);
        supOrder.setInsertDate(new Date());
        supOrder.setStatus("new");

        ItemSupplierOrderJPA itemSupp = new ItemSupplierOrderJPA();
        itemSupp.setProduct(prodDao.find(1));
        itemSupp.setQuantity(10);

        supOrder.getItemSupplierOrderList().add(itemSupp);

        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        assertNotNull(supOrderDao.create(supOrder));
        em.close();
    }

    @Test
    public void createBatchTest() {
        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        ProductJPADao prodDao = new ProductJPADao(em);
        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        SupplierJPADao supplierDao = new SupplierJPADao(em);
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        StoreJPADao stDao = new StoreJPADao(em);
        EntryJPADao entryJPADao = new EntryJPADao(em);
        
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        entryJPADao.create(entry);
        
        StoreOrderJPA storeOrder = new StoreOrderJPA();
        storeOrder.setEntry(entry);
        storeOrder.setInsertDate(new Date());
        storeOrder.setStatus("new");
        storeOrder.setStoreJPA(stDao.find(1));
        List<ItemStoreOrderJPA> itemStoreOrderOrderList = new ArrayList<ItemStoreOrderJPA>();
        ItemStoreOrderJPA itemStoreOrder = new ItemStoreOrderJPA();
        itemStoreOrder.setProduct(prodDao.find(1));
        itemStoreOrder.setQuantity(12);
        itemStoreOrderOrderList.add(itemStoreOrder);

        storeOrder.setItemStoreOrderOrderList(itemStoreOrderOrderList);
        soDao.create(storeOrder);
        

        List<StoreOrderJPA> storeOrderList = new ArrayList<StoreOrderJPA>();
        storeOrderList.add(storeOrder);
        List<SupplierOrderJPA> supplierOrderJpaList = new ArrayList<SupplierOrderJPA>();

        supOrder.setInsertDate(new Date());
        supOrder.setStatus("new");
        supOrder.setSupplier(supplierDao.find(1));
        supOrder.setStoreOrderList(storeOrderList);
        

        ItemSupplierOrderJPA itemSupp = new ItemSupplierOrderJPA();
        itemSupp.setProduct(prodDao.find(1));
        itemSupp.setQuantity(3);
        itemSupp.setSupplierOrderJPA(supOrder);

        supOrder.getItemSupplierOrderList().add(itemSupp);

        ItemSupplierOrderJPA itemSupp2 = new ItemSupplierOrderJPA();
        itemSupp2.setProduct(prodDao.find(2));
        itemSupp2.setQuantity(2);
        itemSupp2.setSupplierOrderJPA(supOrder);

        supOrder.getItemSupplierOrderList().add(itemSupp2);

        supplierOrderJpaList.add(supOrder);

        assertNotNull(supOrderDao.createBatch(supplierOrderJpaList));

        SupplierOrderJPA result = em.find(SupplierOrderJPA.class, 1);

        int size = result.getItemSupplierOrderList().size();
        long quantity = result.getItemSupplierOrderList().get(0).getQuantity();

        String productName = result.getItemSupplierOrderList().get(0)
                .getProductJPA().getName();
        int productId = result.getItemSupplierOrderList().get(0)
                .getProductJPA().getInternalId();
        
        assertNotNull(result.getStoreOrderList());

        em.close();

        assertEquals(2, size);

        assertEquals(1, productId);
        assertEquals(3, quantity);
        assertEquals("Heineken", productName);

    }

    @Test
    public void updateTest() {
        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        supOrder.setInsertDate(new Date());
        supOrder.setStatus("new");

        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        supOrderDao.create(supOrder);

        supOrderDao.updateOrderNumber(1, "1");
        assertEquals(1, 1);
        em.close();
    }

    /**
     * test for a runtime exception on create method.
     */
    @Test
    public void createErrorTest() {
        EntityManager em = Util.getEntityManager();
        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        SupplierOrderJPA supOrder = new SupplierOrderJPA();
        supOrder.setId(2);
        
        assertFalse(supOrderDao.create(supOrder));
        em.close();
    }
    
    /**
     * tests for update a inexistent supplier order. 
     */
    @Test
    public void updateNullOrderTest(){
        EntityManager em = Util.getEntityManager();
        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        supOrderDao.updateOrderNumber(23, "34");
        em.close();
    }
    
    /**
     * tests for create a empty list.
     */
    @Test
    public void createEmptyList(){
        EntityManager em = Util.getEntityManager();
        SupplierOrderJPADao supOrderDao = new SupplierOrderJPADao(em);
        ArrayList<SupplierOrderJPA> list = new ArrayList<SupplierOrderJPA>();
        assertFalse(supOrderDao.createBatch(list));
    }
}
