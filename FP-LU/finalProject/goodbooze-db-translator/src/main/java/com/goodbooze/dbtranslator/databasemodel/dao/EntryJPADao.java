package com.goodbooze.dbtranslator.databasemodel.dao;

import javax.persistence.EntityManager;

import com.goodbooze.dbtranslator.databasemodel.model.EntryJPA;

/**
 * The class EntryJPADao.
 * @author Luiz Pessanha
 *
 */
public class EntryJPADao {
    /** The entity manager for db access. */
    private EntityManager em;

    /**
     * Instantiates a new product jpa dao.
     *
     * @param em the EntityManager
     */
    public EntryJPADao(EntityManager em) {
        this.em = em;
    }

    /**
     * Creates a entry element in db table named entry.
     *
     * @param entry the entry to be persisted
     * @return true, if successful
     */
    public boolean create(EntryJPA entry) {
        try {
            em.getTransaction().begin();
            em.persist(entry);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * find an entry element in data base.
     * @param id the entry id
     * @return the requested entry or null if not found
     */
    public EntryJPA findById(int id) {
        return em.find(EntryJPA.class, id);
    }

}
