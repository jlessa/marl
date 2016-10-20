package com.supplier.supplierb.service;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import com.supplier.messages.request.v1.NewOrder;
import com.supplier.messages.response.v1.NewOrderResponse;
import com.supplier.supplierb.connection.ConnectionFactory;
import com.supplier.supplierb.dao.OrderDao;
import com.supplier.supplierb.dao.ProductDao;
import com.supplier.supplierb.model.ItemJPA;
import com.supplier.supplierb.model.OrderJPA;
import com.supplier.supplierb.model.ProductJPA;
import com.supplier.types.v1.Item;
import com.supplier.wsdl.orderrequest.error.v1.Error;
import com.supplier.wsdl.orderrequest.v1_0.ErrorMessage;
import com.supplier.wsdl.orderrequest.v1_0.NewOrderPort;

/**
 * this class implements the soap service generated from wsdl. 
 * This service is responsible to save new orders in data base.
 * 
 * @author Luiz Pessanha
 *
 */
public class NewOrderImpl implements NewOrderPort {

	/**
	 * this method is receives a soap new order request, persists order information an returns the order number as response
	 */
	@Override
	public NewOrderResponse newOrderOperation(NewOrder newOrderRequest) throws ErrorMessage{

		OrderJPA orderJPA = new OrderJPA();
		orderJPA.setInsertDate(new Date());
		orderJPA.setStatus("new");

		EntityManager em = ConnectionFactory.getEntityManager();
		ProductDao productDao = new ProductDao(em);

		orderJPA.setItemList(new ArrayList<ItemJPA>());

		for (Item item : newOrderRequest.getItemList().getItem()) {

			ItemJPA itemJPA = new ItemJPA();
			ProductJPA productJPA = productDao.find(item.getProduct().getId());

			if(productJPA != null){
				itemJPA.setProduct(productJPA);
				itemJPA.setQuantity(item.getQuantity());
				itemJPA.setOrder(orderJPA);
				orderJPA.getItemList().add(itemJPA);	
			}else {
				Error error = new Error();
				error.setMessage("Product id: "+ item.getProduct().getId()  +" not found!");
				throw new ErrorMessage("Error", error);
			}

		}

		OrderDao orderDao = new OrderDao(em);
		orderDao.create(orderJPA);

		em.close();

		NewOrderResponse response = new NewOrderResponse();
		response.setOrderNumber(orderJPA.getId());

		return response;
	}

}