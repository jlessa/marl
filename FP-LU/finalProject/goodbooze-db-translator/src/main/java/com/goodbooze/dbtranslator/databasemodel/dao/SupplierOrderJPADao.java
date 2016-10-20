package com.goodbooze.dbtranslator.databasemodel.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.goodbooze.dbtranslator.databasemodel.model.SupplierOrderJPA;
import com.goodbooze.dbtranslator.processor.SaveSupplierOrderProcessor;
import com.sun.istack.logging.Logger;

/**
 * The Class SupplierOrderJPADao.
 */
public class SupplierOrderJPADao {

    /** The EntityManager. */
    private EntityManager em;

    /**
     * Instantiates a new supplier order jpa dao.
     *
     * @param em
     *            the EntityManager
     */
    public SupplierOrderJPADao(EntityManager em) {
        this.em = em;
    }

    /**
     * Creates a supplier order in database.
     *
     * @param supOrder
     *            the supplier order
     * @return true, if successful
     */
    public boolean create(SupplierOrderJPA supOrder) {
        try {
            em.getTransaction().begin();
            em.persist(supOrder);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * Find a supplier order in database.
     *
     * @param id
     *            the supplier order id
     * @return the supplier order jpa
     */
    public SupplierOrderJPA find(int id) {
        return em.find(SupplierOrderJPA.class, id);
    }

    /**
     * update the order number and also the update date.
     *
     * @param id
     *            Supplier Order Id
     * @param orderNumber
     *            the order number
     * @return true, if successful
     */
    public boolean updateOrderNumber(int id, String orderNumber) {

        SupplierOrderJPA supOrder = em.find(SupplierOrderJPA.class, id);
        if (supOrder != null) {
            em.getTransaction().begin();
            supOrder.setOrderNumber(orderNumber);
            supOrder.setUpdateDate(new Date());
            em.merge(supOrder);
            em.getTransaction().commit();
            return true;
        }
        return false;

    }

    /**
     * Creates a list of supplier order in database.
     *
     * @param list
     *            the list
     * @return true, if successful
     */
    public boolean createBatch(List<SupplierOrderJPA> list) {

        Logger log = Logger.getLogger(SaveSupplierOrderProcessor.class);
        log.info("tamanho da lista = " + list.size());

        if (!list.isEmpty()) {

            em.getTransaction().begin();

            for (SupplierOrderJPA supplierOrderJPA : list) {

                em.persist(supplierOrderJPA);
            }
            em.getTransaction().commit();
            return true;
        }
        return false;
    }
}
