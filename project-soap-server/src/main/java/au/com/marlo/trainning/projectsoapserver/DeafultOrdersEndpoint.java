package au.com.marlo.trainning.projectsoapserver;


import java.util.List;
import javax.jws.WebService;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.trainning.projectsoapserver.handler.ContractToEntityHandler;
import au.com.marlo.trainning.projectsoapserver.handler.EntitytoContractHandler;
import au.com.marlo.trainning.service.orders.Orders;
import au.com.marlo.trainning.xmltrainning.client.Client;
import au.com.marlo.trainning.xmltrainning.order.Order;
import au.com.marlo.trainning.xmltrainning.request.Request;
import au.com.marlo.trainning.xmltrainning.response.ObjectFactory;
import au.com.marlo.trainning.xmltrainning.response.Response;






@WebService(portName="OrdersSOAP",serviceName="Orders", 
endpointInterface="au.com.marlo.trainning.service.orders.Orders",
targetNamespace="http://www.marlo.com.au/trainning/service/Orders/")
public class DeafultOrdersEndpoint implements Orders {
			
	
	final static String PERSISTENCE_UNIT = "JPA";
	ModelDao modelDao; 
	
	@Override
	public Response processOrderPlacement(Request orderInquiry) {		
		ObjectFactory responseFactory =  new ObjectFactory();		
		modelDao = new ModelDao(PERSISTENCE_UNIT);		
		Response response = responseFactory.createResponse();				
		Client client = orderInquiry.getClient();				
		List<Order> orders = EntitytoContractHandler.convertListOrder(modelDao.getOrdersByUser(ContractToEntityHandler.convertClient(client)));
		
		for (Order order : orders){
			response.getOrder().add(order);
		}
				
		return response;
	}

}
