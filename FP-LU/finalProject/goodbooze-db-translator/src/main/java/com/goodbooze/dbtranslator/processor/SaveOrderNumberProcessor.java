package com.goodbooze.dbtranslator.processor;

import javax.persistence.EntityManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.goodbooze.dbtranslator.databasemodel.connect.ConnectionFactory;
import com.goodbooze.dbtranslator.databasemodel.dao.SupplierOrderJPADao;
import com.goodbooze.messages.dbtranslator.saveordernumbermsg.v1.UpdateOrderNumber;

/**
 * The Class SaveOrderNumberProcessor updates the ordernumber column in database
 * table supplierorder.
 */
public class SaveOrderNumberProcessor implements Processor {

    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        UpdateOrderNumber updateOrderNumber = exchange.getIn().getBody(
                UpdateOrderNumber.class);

        EntityManager em = ConnectionFactory.getEntityManager();
        SupplierOrderJPADao supplierOrderJpaDao = new SupplierOrderJPADao(em);

        supplierOrderJpaDao.updateOrderNumber(
                updateOrderNumber.getSupplierOrderId(),
                updateOrderNumber.getOrderNumber());

    }

}
