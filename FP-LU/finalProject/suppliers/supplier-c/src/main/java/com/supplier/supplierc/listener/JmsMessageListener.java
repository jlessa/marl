package com.supplier.supplierc.listener;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.supplier.supplierc.connection.ConnectionFactory;
import com.supplier.supplierc.dao.OrderDao;
import com.supplier.supplierc.dao.ProductDao;
import com.supplier.supplierc.messages.request.v1.Item;
import com.supplier.supplierc.messages.request.v1.NewOrder;
import com.supplier.supplierc.messages.response.v1.Response;
import com.supplier.supplierc.model.ItemJPA;
import com.supplier.supplierc.model.OrderJPA;
import com.supplier.supplierc.model.ProductJPA;

public class JmsMessageListener {

	/**
	 * This method is responsible to handle the received request message,  by persisting its data 
	 * 
	 * @param message
	 * @return String
	 * @throws JAXBException
	 * @throws JMSException
	 */
	public String handleMessage(String message) throws JAXBException, JMSException {
		
			//create an unmarshaller for the request 
			JAXBContext requestJc = JAXBContext.newInstance("com.supplier.supplierc.messages.request.v1");
			Unmarshaller unmarshaller = requestJc.createUnmarshaller();

			StringReader reader = new StringReader(message);
			NewOrder newOrder = (NewOrder) unmarshaller.unmarshal(reader);
			
			
			EntityManager em = ConnectionFactory.getEntityManager();
			OrderJPA order = new OrderJPA();
			ProductDao productDao = new ProductDao(em);
			OrderDao orderDao = new OrderDao(em);
			order.setInsertDate(new Date());
			order.setStatus("new");
			
			ArrayList<ItemJPA> itemList = new ArrayList<ItemJPA>();
			
			for (Item item : newOrder.getItens().getItem()) {
				ItemJPA itemJpa = new ItemJPA();
				ProductJPA productJpa = productDao.find(item.getProduct().getId());
				itemJpa.setQuantity(item.getQuantity());
				itemJpa.setProduct(productJpa);
				itemJpa.setOrder(order);
				itemList.add(itemJpa);				
			}
			
			order.setItemList(itemList);
			
			//persisting order
			orderDao.create(order);
			
			//creating a response message
			Response response = new Response();
			response.setOrderNumber(order.getId());
			
			//create a marshaller for response
			JAXBContext responseJc = JAXBContext.newInstance("com.supplier.supplierc.messages.response.v1");
			Marshaller marshaller = responseJc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			 StringWriter writer = new StringWriter();
			marshaller.marshal(response, writer);
			
			//close entity manager
			em.close();
			
			return writer.toString();


	}

}
