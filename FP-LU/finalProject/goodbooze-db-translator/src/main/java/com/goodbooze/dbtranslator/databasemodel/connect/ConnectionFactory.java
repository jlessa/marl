package com.goodbooze.dbtranslator.databasemodel.connect;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * A singleton factory for creating Connection objects for jpa entity manager.
 */
public class ConnectionFactory {
    
    /** The EntityManagerFactory. */
    private static volatile EntityManagerFactory emf;

    /**
     * Gets entity manager. objects
     *
     * @return the entity manager
     */
    public static EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("liquorStore-PU");
        }
        return emf.createEntityManager();
    }

    /**
     * Close factory.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
}
