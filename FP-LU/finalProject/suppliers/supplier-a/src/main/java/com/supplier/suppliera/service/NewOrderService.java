package com.supplier.suppliera.service;

import java.util.Date;

import javax.persistence.EntityManager;

import com.supplier.suppliera.bean.NewOrderRequest;
import com.supplier.suppliera.bean.NewOrderResponse;
import com.supplier.suppliera.connection.ConnectionFactory;
import com.supplier.suppliera.dao.OrderDao;
import com.supplier.suppliera.model.ItemJPA;
import com.supplier.suppliera.model.OrderJPA;

/**
 * This class is responsible to create a new order from a rest received request
 * 
 * @author Luiz Pessanha
 *
 */
public class NewOrderService {

	/**
	 * Creates a new order from received parameter, by persisting the informations in data base;
	 * Returns the order number
	 * @param  newOrderRequest
	 * @return NewOrderResponse
	 */
	public NewOrderResponse createOrder(NewOrderRequest newOrderRequest){
		
		EntityManager em = ConnectionFactory.getEntityManager();
		
		OrderJPA order = new OrderJPA();
		order.setInsertDate(new Date());
		order.setStatus("new");
		order.setItemList(newOrderRequest.getItens());
		
		for (ItemJPA item : order.getItemList()) {
			em.merge(item.getProduct());
			item.setOrder(order);
		}
		
		OrderDao orderDao = new OrderDao(em);
		orderDao.create(order);
		
		
		NewOrderResponse response = new NewOrderResponse();
		response.setOrderNumber(order.getId());
		em.close();
		
		return response;
		
	}
}
