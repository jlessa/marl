package com.supplier.supplierb.testutil;

import javax.persistence.EntityManager;

import com.supplier.supplierb.connection.ConnectionFactory;
import com.supplier.supplierb.model.ProductJPA;

public class Util {
	
	public static void cleanUpBase() throws InterruptedException{
		ConnectionFactory.close();
		//wait 3 seconds for data base get closed
		Thread.sleep(3000);
	}
	
	public static void setUp(){
		EntityManager em = ConnectionFactory.getEntityManager();
		ProductJPA product = new ProductJPA();
		product.setName("Paulaner");
		
		em.getTransaction().begin();
		em.persist(product);
		em.getTransaction().commit();
		em.close();
	}
	
}
