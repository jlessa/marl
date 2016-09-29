package au.com.marlo.training.main;

import java.util.List;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;


public class Main {
	final static String PERSISTENCE_UNIT = "JPA";
	
	public static void main (String [] args){						
		
		ModelDao modelDao = new ModelDao(PERSISTENCE_UNIT);
		
		Client client = (Client) modelDao.getById(Client.class, 1);
		List <Order> orders= modelDao.getOrdersByUser(client);		
		
		for(Order or: orders){
			System.out.println("============================");
			System.out.println(or.getOrderNumber());
			System.out.println(or.getClient().getName());
			System.out.println(or.getClient().getAge());
			System.out.println(or.getClient().getGender());
			System.out.println(or.getClient().getDocumentId());
			System.out.println(or.getClient().getAvailable_account_credit());			
			System.out.println("============================");
		}
		
		List<Product> products = modelDao.getAllProducts();
		
		for(Product product : products){
			System.out.println("============================");
			System.out.println(product.getCode());
			System.out.println(product.getCategory());
			System.out.println(product.getDescription());
			System.out.println(product.getPrice());			
			System.out.println("============================");
		}
				
		modelDao.close();
		
	}
}
