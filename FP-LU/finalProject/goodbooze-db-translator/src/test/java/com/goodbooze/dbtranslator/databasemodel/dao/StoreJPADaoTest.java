package com.goodbooze.dbtranslator.databasemodel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;
import com.goodbooze.dbtranslator.testutil.Util;

public class StoreJPADaoTest {
    
    
    @After
    public void closeEntityManager() throws InterruptedException{
        Util.cleanUpBase();
    }
    
    /*
     * checks a test can be created and found 
     */
    @Test
    public void createAndFindTest(){
        EntityManager em = Util.getEntityManager();
        StoreJPADao storeJPADao = new StoreJPADao(em);
        
        StoreJPA storeJPA = new StoreJPA();
        storeJPA.setName("StoreJPA A");
        assertTrue(storeJPADao.createStore(storeJPA)); 
        
        em.merge(storeJPA);
        
        StoreJPA result = storeJPADao.find(storeJPA.getId());
        result.getId();
        result.getName();
        assertEquals("StoreJPA A", result.getName());
        em.close();
    }
    
    /**
     * tests for catch run time exception
     */
    @Test
    public void findErrorTest(){
        EntityManager em = Util.getEntityManager();
        StoreJPADao storeJPADao = new StoreJPADao(em);
        StoreJPA store = new StoreJPA();
        assertNull(storeJPADao.find(store.getId()));
        em.close();
    }
    
    /**
     * tests for catch run time exception on create method
     */
    @Test
    public void createErrorTest(){
        EntityManager em = Util.getEntityManager();
        StoreJPADao storeJPADao = new StoreJPADao(em);
        
        StoreJPA storeJPA = new StoreJPA();
        storeJPA.setId(2);
        assertFalse(storeJPADao.createStore(storeJPA));
        em.close();
    }
}
