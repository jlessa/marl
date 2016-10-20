package com.goodbooze.dbtranslator.databasemodel.dao;

import javax.persistence.EntityManager;

import com.goodbooze.dbtranslator.databasemodel.model.SupplierJPA;


/**
 * The Class SupplierJPADao.
 */
public class SupplierJPADao {
    
    /** The em. */
    private EntityManager em;
    
    /**
     * Instantiates a new supplier jpa dao.
     *
     * @param em the em
     */
    public SupplierJPADao(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Find a supplier in database.
     *
     * @param id the supplier id
     * @return the supplier jpa
     */
    public SupplierJPA find(int id){
        return em.find(SupplierJPA.class, id);
    }
}
