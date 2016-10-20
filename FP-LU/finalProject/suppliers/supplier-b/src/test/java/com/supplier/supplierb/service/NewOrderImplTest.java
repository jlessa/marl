package com.supplier.supplierb.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supplier.messages.request.v1.NewOrder;
import com.supplier.messages.response.v1.NewOrderResponse;
import com.supplier.supplierb.testutil.Util;
import com.supplier.types.v1.Item;
import com.supplier.types.v1.ItemList;
import com.supplier.types.v1.Product;
import com.supplier.wsdl.orderrequest.v1_0.ErrorMessage;

public class NewOrderImplTest {

	@Before
	public void setUp(){
		Util.setUp();
	}
	
	@After
	public void cleanUpBase() throws InterruptedException{
		Util.cleanUpBase();
	}
	/**
	 * this test checks service answer, by sending an request object to service method and asserting the returned response order number value  
	 * @throws ErrorMessage 
	 */
	@Test
	public void testService() throws ErrorMessage{
		
		
		NewOrderImpl service = new NewOrderImpl();
		NewOrder request = new NewOrder();
		ItemList itemList = new ItemList();
		Product product = new Product();
		Item item = new Item();
		
		product.setId(1);
		product.setName("Paullaner");
		
		item.setQuantity(2);
		item.setProduct(product);
		
		itemList.getItem().add(item);
		
		request.setItemList(itemList);
		
		NewOrderResponse response  = service.newOrderOperation(request);
		
		
		assertEquals(1, response.getOrderNumber());
		
	}
}
