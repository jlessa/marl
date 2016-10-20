package com.supplier.suppliera.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 * Connection factory for jpa entity manager 
 * 
 * 
 * @author Luiz Pessanha
 *
 */
public class ConnectionFactory {
	private static EntityManagerFactory emf;

	private ConnectionFactory() {

	}

	public static EntityManager getEntityManager() {
		if (emf == null || !emf.isOpen()) {
			emf = Persistence.createEntityManagerFactory("suppliera-PU");
		}
		return emf.createEntityManager();
	}

	public static void close() {
		if (emf.isOpen() || emf != null) {
			emf.close();
		}
	}

}
