package com.goodbooze.dbtranslator.databasemodel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goodbooze.dbtranslator.databasemodel.model.EntryJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ItemStoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.dbtranslator.testutil.Util;

public class StoreOrderJPADaoTest {

    
    /**
     * initialize database with some needed entries by tests.
     * this methods execute before every unit test in this class
     */
    @Before
    public void setUpTest(){
        EntityManager em = Util.getEntityManager();
        
        StoreJPA store = new StoreJPA();
        store.setName("Store A");
        
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        em.getTransaction().begin();
        
        em.persist(entry);
        em.persist(store);
        em.getTransaction().commit();
        em.close();
    }
    
    @After
    public void closeEntityManager() throws InterruptedException{
        Util.cleanUpBase();
    }

    
    
    /**
     * verify if storeOrderJPA is inserted in database
     * after try to retrieve the inserted storeOrderJpa
     */
    @Test
    public void createTestAndFind(){
        EntityManager em = Util.getEntityManager();
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        StoreJPADao storeDao = new StoreJPADao(em);
        
        StoreOrderJPA sOrder = new StoreOrderJPA();
        EntryJPADao entryDao = new EntryJPADao(em);
        
        sOrder.setInsertDate(new Date());
        sOrder.setStatus("new");
        sOrder.setEntry(new EntryJPA());
        sOrder.getItemStoreOrderOrderList().add(new ItemStoreOrderJPA());
        sOrder.setStore(new StoreJPA());
        sOrder.setUpdateDate(new Date());
        sOrder.setStore(storeDao.find(1));
        
        sOrder.setEntry(entryDao.findById(1));
        
        assertTrue(soDao.create(sOrder));
                
        
        StoreOrderJPA result =soDao.find(sOrder.getId());
        result.getStatus();
        result.getInsertDate();
        result.getUpdateDate();
        result.getItemStoreOrderOrderList();
        result.getEntry();
        assertEquals("Store A",result.getStore().getName());
        assertNotNull(result);
        em.close();
    }
    
    /**
     * tests for catch a runtime exception on create method.
     */
    @Test
    public void createError(){
        EntityManager em = Util.getEntityManager();
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        StoreOrderJPA sOrder = new StoreOrderJPA();
        sOrder.setId(3);
        
        assertFalse(soDao.create(sOrder));
        
    }
    
    /**
     * tests create a list of store with success.
     */
    @Test
    public void createBatchTest(){
        EntityManager em = Util.getEntityManager();
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        StoreJPADao storeDao = new StoreJPADao(em);
        EntryJPADao entryDao = new EntryJPADao(em);
        ArrayList<StoreOrderJPA> list = new ArrayList<StoreOrderJPA>();
        StoreJPA storeJPA = storeDao.find(1);
        EntryJPA entry = entryDao.findById(1);
        StoreOrderJPA sOrder = new StoreOrderJPA();
        sOrder.setInsertDate(new Date());
        sOrder.setStatus("new");
        sOrder.setStoreJPA(storeJPA);
        sOrder.setStoreJPA(storeJPA);
        sOrder.setEntry(entry);
        list.add(sOrder);
        
        StoreOrderJPA sOrder2 = new StoreOrderJPA();
        sOrder2.setInsertDate(new Date());
        sOrder2.setStatus("new");
        sOrder2.setStoreJPA(storeJPA);
        sOrder2.setEntry(entry);
        list.add(sOrder2);
        
        assertTrue(soDao.createBatch(list));
        em.close();
    }
    
    /**
     * Tests for create a list with incorrect values
     */
    @Test
    public void createBatchErrorTest(){
        EntityManager em = Util.getEntityManager();
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        StoreOrderJPA sOrder = new StoreOrderJPA();
        ArrayList<StoreOrderJPA> list = new ArrayList<StoreOrderJPA>();
        list.add(sOrder);
        assertFalse(soDao.createBatch(list));
        
        StoreJPADao storeDao = new StoreJPADao(em);
        StoreJPA storeJPA = storeDao.find(1);
        sOrder.setInsertDate(new Date());
        sOrder.setStatus("new");
        sOrder.setStoreJPA(storeJPA);
        sOrder.setStoreJPA(storeJPA);
        
        assertFalse(soDao.createBatch(list));
        
        em.close();
    }
    
    /**
     * tests for create an empty list
     */
    @Test
    public void createEmptyListTest(){
        EntityManager em = Util.getEntityManager();
        StoreOrderJPADao soDao = new StoreOrderJPADao(em);
        ArrayList<StoreOrderJPA> list = new ArrayList<StoreOrderJPA>();
        assertFalse(soDao.createBatch(list));
        em.close();
    }
}
