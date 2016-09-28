package au.com.marlo.training.helper;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;

public class Initializer {
	public static void initDb(String persistenceUnit, ModelDao model){
		
		
		Client client = new Client();
		client.setName("Teste");
		client.setAge(20);
		client.setAvailable_account_credit(50);
		client.setGender("Male");			
		
		Product product = new Product();
		Product product2 = new Product();
		
		product.setDescription("Geladeira");
		product.setCategory("Domestico");
		product.setPrice(1000.0f);
		
		product2.setDescription("Radio");
		product2.setCategory("Eletronico");
		product2.setPrice(100.0f);
											
		Order order = new Order();
		
		order.setClient(client);										
		order.addProduct(product);
		
		Order order2 = new Order();
		
		order.setClient(client);									
		order.addProduct(product);
		order.addProduct(product2);
		
		model.save(client);
		model.save(product);
		model.save(product2);
		model.save(order);
		model.save(order2);
			

	}
}
