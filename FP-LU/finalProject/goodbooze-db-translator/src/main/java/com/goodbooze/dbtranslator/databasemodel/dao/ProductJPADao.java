package com.goodbooze.dbtranslator.databasemodel.dao;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierJPA;


/**
 * The Class ProductJPADao.
 */
public class ProductJPADao {
    
    /** The entity manager for db access. */
    private EntityManager em;

    /**
     * Instantiates a new product jpa dao.
     *
     * @param em the EntityManager
     */
    public ProductJPADao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find.a product in database.
     *
     * @param id the product id
     * @return the product jpa
     */
    public ProductJPA find(int id) {
        return em.find(ProductJPA.class, id);
    }

    /**
     * retrieve pair value of productJPA's id and it's supplierJPA.
     *
     * @param ids the ids
     * @return hashmap containing (productList id,supplierJPA) values
     * @return null if supplier not found
     */
    public HashMap<Integer, SupplierJPA> getSupplierByProductId(
            List<Integer> ids) {
        HashMap<Integer, SupplierJPA> result = new HashMap<Integer, SupplierJPA>();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < ids.size(); i++)
                result.put(ids.get(i), em.find(ProductJPA.class, ids.get(i))
                        .getSupplierJPA());
            em.getTransaction().commit();
            return result;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return null;
        }
    }

}
