package com.supplier.supplierc.listener;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supplier.supplierc.messages.request.v1.Item;
import com.supplier.supplierc.messages.request.v1.ItemList;
import com.supplier.supplierc.messages.request.v1.NewOrder;
import com.supplier.supplierc.messages.request.v1.Product;
import com.supplier.supplierc.messages.response.v1.Response;
import com.supplier.supplierc.testutil.Util;

public class JmsMessageListenerTest {
	
	/**
	 * initialize data base with necessary values for tests
	 */
	@Before
	public void setUpBase(){
		Util.setUp();
	}
	
	/**
	 * clean data base
	 * @throws InterruptedException
	 */
	@After
	public void cleanBase() throws InterruptedException{
		Util.cleanUpBase();
	}

	/**
	 * create a new order request and pass it to handleMessage method, after assert the returned response
	 * @throws JAXBException
	 * @throws JMSException
	 */
	@Test
	public void handleMessageTest() throws JAXBException, JMSException {
		Product product = new Product();
		product.setId(1);
		product.setName("Therezopolis");

		Item item = new Item();
		item.setProduct(product);
		item.setQuantity(2);

		ItemList itemList = new ItemList();
		itemList.getItem().add(item);

		NewOrder order = new NewOrder();
		order.setItens(itemList);

		JAXBContext requestJc = JAXBContext
				.newInstance("com.supplier.supplierc.messages.request.v1");
		Marshaller marshaller = requestJc.createMarshaller();

		StringWriter writer = new StringWriter();
		marshaller.marshal(order, writer);

		JmsMessageListener listener = new JmsMessageListener();

		String result = listener.handleMessage(writer.toString());

		JAXBContext responseJc = JAXBContext.newInstance("com.supplier.supplierc.messages.response.v1");
		Unmarshaller unmarshaller = responseJc.createUnmarshaller();
		
		StringReader reader = new StringReader(result);
		Response response = (Response) unmarshaller.unmarshal(reader);
		
		assertEquals(1, response.getOrderNumber());
	}
}
