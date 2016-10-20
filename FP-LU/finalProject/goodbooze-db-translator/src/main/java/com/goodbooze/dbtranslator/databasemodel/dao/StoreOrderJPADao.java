package com.goodbooze.dbtranslator.databasemodel.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;

/**
 * The Class StoreOrderJPADao.
 */
public class StoreOrderJPADao {

    private static final int CLEAN_CACHE_COUNT = 50;

    /** The EntityManager. */
    private EntityManager em;

    /**
     * Instantiates a new store order jpa dao.
     *
     * @param em
     *            the EntityManager
     */
    public StoreOrderJPADao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find a store order in database.
     *
     * @param id
     *            the id
     * @return the store order jpa
     */
    public StoreOrderJPA find(int id) {
        return em.find(StoreOrderJPA.class, id);
    }

    /**
     * Creates a new store order registry in database.
     *
     * @param sOrder
     *            the store order
     * @return true, if successful
     */
    public boolean create(StoreOrderJPA sOrder) {
        try {
            em.getTransaction().begin();
            em.persist(sOrder);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * Creates a list of store orders.
     *
     * @param list
     *            the list of store orders
     * @return true, if successful
     */
    public boolean createBatch(List<StoreOrderJPA> list) {
        int contador = 0;
        if (!list.isEmpty()) {
            em.getTransaction().begin();
            StoreOrderJPA storeOrder = list.get(contador);
            try {
                while (storeOrder != null) {
                    em.persist(storeOrder);
                    // a cada 50 objetos, faz a sincronizacao e limpa o cache
                    if (contador % CLEAN_CACHE_COUNT == 0) {
                        em.flush();
                        em.clear();
                    }
                    contador++;
                    if (contador == list.size()) {
                        storeOrder = null;
                    } else {
                        storeOrder = list.get(contador);
                    }
                }
                em.getTransaction().commit();
                return true;
            } catch (RollbackException e) {
                return false;
            }
            catch (RuntimeException e) {
                em.getTransaction().rollback();
                return false;
            }

        }
        return false;
    }

}
