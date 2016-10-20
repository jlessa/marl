package com.supplier.supplierc.testutil;

import javax.persistence.EntityManager;

import com.supplier.supplierc.connection.ConnectionFactory;
import com.supplier.supplierc.model.ProductJPA;

/**
 * Auxiliary class for unit tests
 * @author Luiz Pessanha
 *
 */
public class Util {
	
	/**
	 * clean data base 
	 * @throws InterruptedException
	 */
	public static void cleanUpBase() throws InterruptedException{
		ConnectionFactory.close();
		//wait 3 seconds for data base get closed
		Thread.sleep(3000);
	}
	
	/**
	 * initialize data base with necessary value for unit tests
	 */
	public static void setUp(){
		EntityManager em = ConnectionFactory.getEntityManager();
		ProductJPA product = new ProductJPA();
		product.setName("Therezopolis");
		
		em.getTransaction().begin();
		em.persist(product);
		em.getTransaction().commit();
		em.close();
	}
	
}
