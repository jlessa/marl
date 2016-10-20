package com.goodbooze.dbtranslator.databasemodel.dao;

import javax.persistence.EntityManager;

import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;


/**
 * The Class StoreJPADao.
 */
public class StoreJPADao {
    
    /** The EntityManager. */
    private EntityManager em;

    /**
     * Instantiates a new store jpa dao.
     *
     * @param em the em
     */
    public StoreJPADao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find a store in database.
     *
     * @param id the store id
     * @return the store jpa or null if the entity does not exist
     */
    public StoreJPA find(int id) {
            return em.find(StoreJPA.class, id);
    }

    /**
     * Creates the store.
     *
     * @param storeJPA the store jpa
     * @return true, if successful
     */
    public boolean createStore(StoreJPA storeJPA) {
        try {
            em.getTransaction().begin();
            em.persist(storeJPA);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }
}
