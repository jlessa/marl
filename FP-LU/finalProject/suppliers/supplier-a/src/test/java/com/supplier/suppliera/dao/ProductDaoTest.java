package com.supplier.suppliera.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supplier.suppliera.connection.ConnectionFactory;
import com.supplier.suppliera.model.ProductJPA;
import com.supplier.suppliera.testutil.Util;


public class ProductDaoTest {

	@Before
	public void setUp(){
		Util.setUp();
	}
	
	@After
	public void cleanUpBase() throws InterruptedException{
		Util.cleanUpBase();
	}
	
	@Test
	public void findProductTest(){
		EntityManager em = ConnectionFactory.getEntityManager();
		ProductDao productDao = new ProductDao(em);
		
		ProductJPA result = productDao.find(1);
		
		assertNotNull(result);
		
		assertEquals(1, result.getId());
		
		em.close();
	}
}
