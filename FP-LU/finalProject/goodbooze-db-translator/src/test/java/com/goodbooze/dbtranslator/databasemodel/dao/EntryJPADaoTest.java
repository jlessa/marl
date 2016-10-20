package com.goodbooze.dbtranslator.databasemodel.dao;



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
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.dbtranslator.testutil.Util;

public class EntryJPADaoTest {
    
    private static  EntryJPADao entryJPADao;
    
    @Before
    public void setUpTest(){
        EntityManager em = Util.getEntityManager();
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        em.getTransaction().begin();
        em.persist(entry);
        em.getTransaction().commit();
        em.close();
    }
    

    
    @After
    public void cleanUpBase() throws InterruptedException{
        Util.cleanUpBase();
    }
    /**
     * test find the inserted entity in setUp method
     */
    @Test
    public void findTest(){
        EntityManager em = Util.getEntityManager();
        entryJPADao = new EntryJPADao(em);
        EntryJPA result = entryJPADao.findById(1);
        result.getEntryId();
        result.getInsertDate();
        result.getProcessDate();
        result.getStoreOrderList();
        result.getStoreOrderList();
        
        assertNotNull(result);
        

    
        em.close();
    }
    
    /**
     * test create a new entry entity on data base
     */
    @Test
    public void createTest(){
        EntityManager em = Util.getEntityManager();
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        entry.setProcessDate(new Date());
        entry.setStoreOrderList(new ArrayList<StoreOrderJPA>());
        EntryJPADao dao = new EntryJPADao(em);
        assertTrue(dao.create(entry));
        em.close();
    }
    
    /**
     * test for catch run time exception.
     */
    @Test
    public void createErrorTest(){
        EntityManager em = Util.getEntityManager();
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        entry.setEntryId(3);
        EntryJPADao dao = new EntryJPADao(em);
        assertFalse(dao.create(entry));
        em.close();
    }
    
}
