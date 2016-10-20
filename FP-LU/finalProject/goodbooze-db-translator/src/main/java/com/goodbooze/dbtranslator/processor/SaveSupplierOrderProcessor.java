package com.goodbooze.dbtranslator.processor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.goodbooze.dbtranslator.databasemodel.connect.ConnectionFactory;
import com.goodbooze.dbtranslator.databasemodel.dao.ProductJPADao;
import com.goodbooze.dbtranslator.databasemodel.dao.StoreOrderJPADao;
import com.goodbooze.dbtranslator.databasemodel.dao.SupplierJPADao;
import com.goodbooze.dbtranslator.databasemodel.dao.SupplierOrderJPADao;
import com.goodbooze.dbtranslator.databasemodel.model.ItemSupplierOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierOrderJPA;
import com.goodbooze.messages.dbtranslator.savesupplierorder.v1.SaveSupplierOrder;
import com.goodbooze.messages.dbtranslator.savesupplierorder.v1.SupplierOrder;
import com.goodbooze.types.typesdefinition.v1.ItemSupplier;




/**
 * The Class SaveSupplierOrderProcessor saves a new supplier order in database.
 */
public class SaveSupplierOrderProcessor implements Processor {
    
    Logger log = Logger.getLogger(SaveSupplierOrderProcessor.class);
    
    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        SaveSupplierOrder msg = exchange.getIn().getBody(SaveSupplierOrder.class);
        
        List<SupplierOrder> supplierOrderList = msg.getSupplierOrderList().getSupplierOrder(); 
        List<SupplierOrderJPA> supplierOrderJpaList = new ArrayList<SupplierOrderJPA>();
        
        //instantiating daos  
        EntityManager em = ConnectionFactory.getEntityManager();
        StoreOrderJPADao storeOrderJPAdao = new StoreOrderJPADao(em);
        ProductJPADao productJpaDao = new ProductJPADao(em);
        SupplierJPADao supplierJpaDao = new SupplierJPADao(em);
        SupplierOrderJPADao supplierOrderJpaDao = new SupplierOrderJPADao(em);
        
        //transforming SupplierOrder to SuppleirOrderJPA
        for(SupplierOrder supplierOrder : supplierOrderList){
            SupplierOrderJPA supplierOrderJpa = new SupplierOrderJPA();
            supplierOrderJpa.setInsertDate(supplierOrder.getInsertDate().toGregorianCalendar().getTime());
            supplierOrderJpa.setStatus(supplierOrder.getStatus());
            //supplierOrderJpa.setStoreOrderList(new ArrayList<StoreOrderJPA>());
            supplierOrderJpa.setItemSupplierOrderList(new ArrayList<ItemSupplierOrderJPA>());
            supplierOrderJpa.setSupplier(supplierJpaDao.find(supplierOrder.getSupplierId()));
            
            ArrayList<StoreOrderJPA> storeOrderList = new ArrayList<StoreOrderJPA>();
            
            //transforming StoreOrderList in StoreOrdeJPAList
            for(int storeOrderId : supplierOrder.getStoreOrderList().getStoreOrderId()){
                StoreOrderJPA storeOrderJpa = storeOrderJPAdao.find(storeOrderId);
                storeOrderList.add(storeOrderJpa);                        
            }
            
            supplierOrderJpa.setStoreOrderList(storeOrderList);
            
            //getting item supplier order from canonical model
            for (ItemSupplier itemSupplier : supplierOrder.getItemSupplierOrderList().getItemSupplier()) {
                ItemSupplierOrderJPA itemSupplierOrderJpa = new ItemSupplierOrderJPA();
                
                ProductJPA product = productJpaDao.find(itemSupplier.getProduct().getInternalId());

                
                itemSupplierOrderJpa.setProduct(product);
                itemSupplierOrderJpa.setQuantity(itemSupplier.getQuantity());
                itemSupplierOrderJpa.setSupplierOrderJPA(supplierOrderJpa);
                
                supplierOrderJpa.getItemSupplierOrderList().add(itemSupplierOrderJpa);
                
                itemSupplier.getProduct().setProductSupplierId(product.getProductSupplierId());
            }
            
            supplierOrderJpaList.add(supplierOrderJpa);
        }
        
        supplierOrderJpaDao.createBatch(supplierOrderJpaList);
        
        
        //set supplier order id on message
        int index =0;
        for (SupplierOrderJPA supplierOrderJPA : supplierOrderJpaList) {
            supplierOrderList.get(index).setId(supplierOrderJPA.getId());
            index++;
        }
        
        exchange.getIn().setBody(msg);
        em.clear();
    }

}
