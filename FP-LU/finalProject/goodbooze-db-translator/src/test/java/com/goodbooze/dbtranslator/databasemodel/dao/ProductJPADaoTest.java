package com.goodbooze.dbtranslator.databasemodel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierJPA;
import com.goodbooze.dbtranslator.testutil.Util;

public class ProductJPADaoTest {


    private static ProductJPADao supDao;

    /**
     * initialize database with some needed entries by tests.
     * this methods execute before every unit test in this class
     */
    @Before
    public  void setUpTest() {
        EntityManager em = Util.getEntityManager();
        
        SupplierJPA sup = new SupplierJPA();
        sup.setName("supplierJPA a");
        
        ProductJPA prod = new ProductJPA();
        prod.setName("Stella");
        prod.setSupplierJPA(sup);
        
        em.getTransaction().begin();
        em.persist(sup);
        em.persist(prod);
        em.getTransaction().commit();
        
        em.close();
    }
    
    
    /**
     * close the data base after every unit test
     * @throws InterruptedException
     */
    @After
    public void cleanUpBase() throws InterruptedException{
        Util.cleanUpBase();
    }
    
    /**
     * test getSupplierByProductId method.
     */
    @Test
    public void getSuppliersTest(){
        EntityManager em = Util.getEntityManager();
        supDao = new ProductJPADao(em);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        HashMap<Integer, SupplierJPA> result = supDao.getSupplierByProductId(ids);
        result.get(1).getId();
        result.get(1).getName();
        result.get(1).getProductList();
        assertEquals("supplierJPA a", result.get(1).getName());
        em.close();
    }
    
    /**
     * tests for catch runtime exception.
     */
    @Test
    public void getSuppliersErrorTest(){
        EntityManager em = Util.getEntityManager();
        supDao = new ProductJPADao(em);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(3);
        HashMap<Integer, SupplierJPA> result = supDao.getSupplierByProductId(ids);
        
        assertNull(result); 
    }
}
