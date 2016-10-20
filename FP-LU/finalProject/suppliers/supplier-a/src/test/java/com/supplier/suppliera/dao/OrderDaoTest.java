package com.supplier.suppliera.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supplier.suppliera.connection.ConnectionFactory;
import com.supplier.suppliera.model.ItemJPA;
import com.supplier.suppliera.model.OrderJPA;
import com.supplier.suppliera.model.ProductJPA;
import com.supplier.suppliera.testutil.Util;

public class OrderDaoTest {
	
	@Before
	public void setUp(){
		Util.setUp();
	}
	
	@After
	public void cleanUpBase() throws InterruptedException{
		Util.cleanUpBase();
	}
	
	/**
	 * tests for creating an order
	 */
	@Test
	public void createOrderTest(){
		OrderJPA order = new OrderJPA();
		order.setInsertDate(new Date());
		order.setStatus("new");
		EntityManager em = ConnectionFactory.getEntityManager();
		
		ProductDao prodDao = new ProductDao(em);
		ProductJPA product = prodDao.find(1);
				
		ItemJPA item = new ItemJPA();
		item.setQuantity(10);
		item.setProduct(product);
		
		ArrayList<ItemJPA> itens = new ArrayList<ItemJPA>();
		itens.add(item);
		
		order.setItemList(itens);
		
		OrderDao orderDao = new OrderDao(em);
		
		assertTrue(orderDao.create(order));
		
		OrderJPA result = em.find(OrderJPA.class, 1);
		
		assertEquals(10, result.getItemList().get(0).getQuantity());
		
		assertEquals(1, result.getItemList().get(0).getProduct().getId());
	}
}
