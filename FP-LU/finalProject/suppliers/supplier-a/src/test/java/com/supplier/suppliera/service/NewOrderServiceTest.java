package com.supplier.suppliera.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supplier.suppliera.bean.NewOrderRequest;
import com.supplier.suppliera.bean.NewOrderResponse;
import com.supplier.suppliera.connection.ConnectionFactory;
import com.supplier.suppliera.dao.ProductDao;
import com.supplier.suppliera.model.ItemJPA;
import com.supplier.suppliera.model.OrderJPA;
import com.supplier.suppliera.model.ProductJPA;
import com.supplier.suppliera.service.NewOrderService;
import com.supplier.suppliera.testutil.Util;


public class NewOrderServiceTest {

	@Before
	public void setUpBase(){
		Util.setUp();
	}
	
	@After
	public void closeBase() throws InterruptedException{
		Util.cleanUpBase();
	}
	
	/**
	 * tests service method by sending an request object and asserting the response returned by service method.
	 */
	@Test
	public void testNewOrderService(){
		
		EntityManager em = ConnectionFactory.getEntityManager();
		ProductDao productDao = new ProductDao(em);
		//create an request
		ProductJPA product1 = productDao.find(1);
				
		ItemJPA item = new ItemJPA();
		item.setProduct(product1);
		item.setQuantity(32);
		
		ArrayList<ItemJPA> itemList = new ArrayList<ItemJPA>();
		itemList.add(item);
		
		OrderJPA order = new OrderJPA();
		order.setItemList(itemList);
		
		NewOrderRequest newOrderRequest = new NewOrderRequest();
		newOrderRequest.setItens(itemList);
		
		em.close();
		
		NewOrderService service = new NewOrderService();
		//get the service response
		NewOrderResponse response = service.createOrder(newOrderRequest);
		
		assertEquals(1, response.getOrderNumber());
	}
}
