package com.goodbooze.dbtranslator.processor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.goodbooze.dbtranslator.databasemodel.connect.ConnectionFactory;
import com.goodbooze.dbtranslator.databasemodel.dao.EntryJPADao;
import com.goodbooze.dbtranslator.databasemodel.dao.ProductJPADao;
import com.goodbooze.dbtranslator.databasemodel.dao.StoreJPADao;
import com.goodbooze.dbtranslator.databasemodel.model.EntryJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ItemStoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.messages.dbtranslator.savestoreordermsg.v1.SaveStoreOrder;
import com.goodbooze.types.typesdefinition.v1.ItemStoreOrder;
import com.goodbooze.types.typesdefinition.v1.StoreOrder;
import com.goodbooze.types.typesdefinition.v1.StoreOrderList;


/**
 * The Class SaveStoreOrderProcessor saves a new store order in database.
 */
public class SaveStoreOrderProcessor implements Processor{

    
    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        int index =0;
        
        SaveStoreOrder msg = exchange.getIn().getBody(SaveStoreOrder.class);
        //get the root element
        StoreOrderList storelist = msg.getStoreOrderList();
        //get a list of storeOrder from the root element
        List<StoreOrder> storeOrderList = storelist.getStoreOrder();
        //create an ArrayList of the jpa mapped entity StoreOrderJPA
        ArrayList<StoreOrderJPA> storeOrderJPAlist = new ArrayList<StoreOrderJPA>();
        
        EntityManager em = ConnectionFactory.getEntityManager();
        StoreJPADao storeDao = new StoreJPADao(em);
        ProductJPADao productJpaDao = new ProductJPADao(em);
        EntryJPADao entryJPADao = new EntryJPADao(em);
        
        EntryJPA entryJPA = new EntryJPA();
        
        entryJPA.setInsertDate(storeOrderList.get(0).getInsertDate().toGregorianCalendar().getTime());
        
        for (StoreOrder storeOrder : storeOrderList) {
            StoreOrderJPA storeOrderJPA = new StoreOrderJPA();
            List<ItemStoreOrderJPA> itemStoreOrderJPAList = new ArrayList<ItemStoreOrderJPA>();
            
            StoreJPA storeJPA = storeDao.find(storeOrder.getStore().getId());

            
            storeOrderJPA.setInsertDate(storeOrder.getInsertDate().toGregorianCalendar().getTime());
            storeOrderJPA.setStatus(storeOrder.getStatus());
            storeOrderJPA.setEntry(entryJPA);

            for(ItemStoreOrder itemStoreOrder : storeOrder.getItemStoreOrderList().getItemStoreOrder()){
                ItemStoreOrderJPA itemStoreJpa = new ItemStoreOrderJPA();
                ProductJPA productJpa = productJpaDao.find(itemStoreOrder.getProduct().getInternalId());
                                
                itemStoreJpa.setQuantity(itemStoreOrder.getQuantity());
                itemStoreJpa.setProduct(productJpa);
                itemStoreJpa.setStoreOrder(storeOrderJPA);
                itemStoreOrderJPAList.add(itemStoreJpa);
                //set supplier id to be used in request message
                itemStoreOrder.getProduct().setProductSupplierId(itemStoreJpa.getProduct().getSupplierJPA().getId());
            }
            
            
            storeOrderJPA.setStore(storeJPA);
            storeOrderJPA.setItemStoreOrderOrderList(itemStoreOrderJPAList);
            storeOrderJPAlist.add(storeOrderJPA);
            
        }
        
        entryJPA.setStoreOrderList(storeOrderJPAlist);
        entryJPADao.create(entryJPA);
        
        for(StoreOrderJPA storeOrderJpa : storeOrderJPAlist){
            em.merge(storeOrderJpa);
            storeOrderList.get(index).setId(storeOrderJpa.getId());
            index++;            
        }
        
        exchange.getIn().setBody(msg);
        em.close();
    }

}
