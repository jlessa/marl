package au.com.marlo.training.tests;


import javax.persistence.RollbackException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;


public class ModelDaoTest {
	static ModelDao modelDao;
	
	@BeforeClass
	public static void createModelDao() {
		modelDao = new ModelDao("JPATest");
	}
	
	@AfterClass
	public static void closeModelDao() {
		modelDao.close();
	}
	
	@Test
	public void saveClient() {
		Client client = new Client();
		client.setAge(25);
		client.setAvailable_account_credit(new Float(200));
		client.setGender("M");
		client.setName("Fulano");
		
		modelDao.save(client);
	}
	
	@Test
	public void saveProduct(){
		Product product = new Product();
		product.setDescription("Geladeira");
		product.setCategory("Domestico");
		product.setPrice(1000.0f);
		
		modelDao.save(product);
	}
	
	@Test
	public void saveOrder(){
		Product product = new Product();
		product.setDescription("Geladeira");
		product.setCategory("Domestico");
		product.setPrice(1000.0f);
		
		Client client = new Client();
		client.setAge(25);
		client.setAvailable_account_credit(new Float(200));
		client.setGender("M");
		client.setName("Fulano");
		
		modelDao.save(client);
		modelDao.save(product);
		
		Order order = new Order();
		order.setClient(client);
		order.addProduct(product);
		
		modelDao.save(order);
	}
	
}
