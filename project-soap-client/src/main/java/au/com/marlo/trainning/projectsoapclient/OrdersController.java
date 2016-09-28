package au.com.marlo.trainning.projectsoapclient;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.com.marlo.trainning.service.orders.Orders;
import au.com.marlo.trainning.xmltrainning.client.Client;
import au.com.marlo.trainning.xmltrainning.order.Order;
import au.com.marlo.trainning.xmltrainning.request.Request;
import au.com.marlo.trainning.xmltrainning.response.Response;

@Controller("/processOrderPlacement")
public class OrdersController {
	
	@Autowired
	private Orders orders;
	
	@RequestMapping(method=RequestMethod.GET)
	public String processOrderPlacement(ModelMap model) throws Exception{
		Client client = new Client();
		client.setName("Joao");
		client.setAge(new BigInteger("28"));
		client.setGender("Male");
		client.setDocumentId(new BigInteger("1"));
		client.setAvailableAccountCredit(new BigDecimal("10.00"));
		
		Request request = new Request();
		request.setClient(client);
		
		Response response = orders.processOrderPlacement(request);
		
		for(Order order : response.getOrder()){
			if(order != null){
				System.out.println("User: ");
				model.addAttribute("orderNumber", order.getUser().getName());
				model.addAttribute("userDocument", order.getUser().getDocumentId());
				model.addAttribute("userAge", order.getUser().getAge());
				model.addAttribute("userGender", order.getUser().getGender());
			}
		}		
		
		model.addAttribute("orders", response.getOrder());
									
		return "processOrderPlacement";
	}
}
