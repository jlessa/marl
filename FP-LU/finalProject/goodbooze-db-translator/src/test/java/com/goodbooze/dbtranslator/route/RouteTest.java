package com.goodbooze.dbtranslator.route;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goodbooze.dbtranslator.databasemodel.model.EntryJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ItemSupplierOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.ProductJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreJPA;
import com.goodbooze.dbtranslator.databasemodel.model.StoreOrderJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierJPA;
import com.goodbooze.dbtranslator.databasemodel.model.SupplierOrderJPA;
import com.goodbooze.dbtranslator.testutil.Util;
import com.goodbooze.messages.dbtranslator.savestoreordermsg.v1.SaveStoreOrder;
import com.goodbooze.messages.dbtranslator.savesupplierorder.v1.SaveSupplierOrder;

public class RouteTest extends CamelTestSupport {

    private static final String DIRECT_START_ROUTE = "direct:startRoute";
    private static final String INPUT_SAVE_STOREORDER_MESSAGE = "Route/msg/input/saveStoreOrder.xml";
    private static final String INPUT_SAVE_SUPPLIERORDER_MESSAGE = "Route/msg/input/saveSupplierOrder.xml";
    private static final String INPUT_SAVE_ORDERNUMBER_MESSAGE = "Route/msg/input/saveOrderNumber.xml";
    final static String EXPECTED_MESSAGE_URL = "Route/msg/output/SuppliersResponseMsg.xml";

    @EndpointInject(uri = "mock:activemq:storeOrderQueue")
    protected MockEndpoint mockStoreOrderQueue;
    
    @EndpointInject(uri = "mock:activemq:supplierOrderQueue")
    protected MockEndpoint mockSupplierOrderQueue;

    @Produce(uri = "direct:start")
    protected ProducerTemplate startEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new Route();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:Route/conf/com.goodbooze.dbtranslator.cfg");
        context.addComponent("properties", properties);

        return context;
    }

    /**
     * setup data base before every unit test
     */
    @Before
    public void setUpBase() {
        EntityManager em = Util.getEntityManager();

        StoreJPA store = new StoreJPA();
        store.setName("Store A");

        SupplierJPA supplier = new SupplierJPA();
        supplier.setName("Supplier A");

        ProductJPA product1 = new ProductJPA();
        product1.setName("Heineken");
        product1.setSupplierJPA(supplier);
        

        ProductJPA product2 = new ProductJPA();
        product2.setName("Stella Artois");
        product2.setSupplierJPA(supplier);
        
        EntryJPA entry = new EntryJPA();
        entry.setInsertDate(new Date());
        
        StoreOrderJPA storeOrder = new StoreOrderJPA();
        storeOrder.setEntry(entry);
        storeOrder.setInsertDate(new Date());
        storeOrder.setStatus("new");
        
        
        

        em.getTransaction().begin();
        em.persist(store);
        em.persist(supplier);
        em.persist(product1);
        em.persist(entry);
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.persist(product2);
        em.persist(storeOrder);
        em.getTransaction().commit();

        em.close();

    }

    /**
     * close the data base after every unit test
     * 
     * @throws InterruptedException
     */
    @After
    public void cleanUpBase() throws InterruptedException {
        Util.cleanUpBase();
    }

    /**
     * checks if the store order was correctly persisted
     * 
     * @throws CamelExecutionException
     * @throws IOException
     * @throws JAXBException 
     */
    @Test
    public void testForSaveStoreOrder() throws CamelExecutionException,
            IOException, JAXBException {

        startEndpoint.sendBodyAndHeader(DIRECT_START_ROUTE,
                fileToString(getClass(), INPUT_SAVE_STOREORDER_MESSAGE),
                "type", "saveStoreOrder");

        EntityManager em = Util.getEntityManager();

        StoreOrderJPA storeOrder = em.find(StoreOrderJPA.class, 2);
        String storeName = storeOrder.getStore().getName();
        em.close();
        assertEquals(storeName, "Store A");

        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.dbtranslator.savestoreordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        String resultBody = mockStoreOrderQueue.getExchanges().get(0).getIn().getBody(String.class);
        
        SaveStoreOrder response = (SaveStoreOrder) unmarshaller.unmarshal(new StringReader(resultBody));
        
        int orderId = response.getStoreOrderList().getStoreOrder().get(0).getId();
        assertEquals(2, orderId);
        String storeFromOrderName =    response.getStoreOrderList().getStoreOrder().get(0).getStore().getName();
        assertEquals("Store A", storeFromOrderName);
        
        String productName = response.getStoreOrderList().getStoreOrder().get(0).getItemStoreOrderList().getItemStoreOrder().get(0).getProduct().getName();
        
        assertEquals("Heineken", productName);
        long quantity  = response.getStoreOrderList().getStoreOrder().get(0).getItemStoreOrderList().getItemStoreOrder().get(0).getQuantity();
        assertEquals(2, quantity);
        
    }

    /**
     * checks if a supplier order was correctly saved
     * 
     * @throws CamelExecutionException
     * @throws IOException
     * @throws JAXBException 
     */
    @Test
    public void testForSaveSupplierOrder() throws CamelExecutionException,
            IOException, JAXBException {

        startEndpoint.sendBodyAndHeader(DIRECT_START_ROUTE,
                fileToString(getClass(), INPUT_SAVE_SUPPLIERORDER_MESSAGE),
                "type", "saveSupplierOrder");

        EntityManager em = Util.getEntityManager();

        SupplierOrderJPA supplierOrder = em.find(SupplierOrderJPA.class, 1);
        String supplierName = supplierOrder.getSupplier().getName();
        int size = supplierOrder.getItemSupplierOrderList().size();
        
        for (ItemSupplierOrderJPA itemSupplierOrder : supplierOrder.getItemSupplierOrderList()) {
            long quantity = itemSupplierOrder.getQuantity();        
            String productName = itemSupplierOrder.getProductJPA().getName();
            int productId = itemSupplierOrder.getProductJPA().getInternalId();
            int productSupplierId = itemSupplierOrder.getProductJPA().getProductSupplierId();
            
            if(productName.equalsIgnoreCase("Heineken")){
                assertEquals(1, productId);
                assertEquals(3, quantity);
                assertEquals("Heineken", productName);
                assertEquals(1, productSupplierId);
            }
            
            if(productName.equalsIgnoreCase("Stella Artois")){
                assertEquals(2, productId);
                assertEquals(2, quantity);
                assertEquals("Stella Artois", productName);
                assertEquals(2, productSupplierId);
            }
            
        }
        assertEquals("new",supplierOrder.getStoreOrderList().get(0).getStatus());
        assertEquals(2, size);
        assertEquals("Supplier A", supplierName);
        
        em.close();
        
        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.dbtranslator.savesupplierorder.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        String resultBody = mockSupplierOrderQueue.getExchanges().get(0).getIn().getBody(String.class);
        
        SaveSupplierOrder response = (SaveSupplierOrder) unmarshaller.unmarshal(new StringReader(resultBody));
        
        int orderId = response.getSupplierOrderList().getSupplierOrder().get(0).getId();
        assertEquals(1, orderId);
    }


    /**
     * checks if the order number was correctly persisted on data base
     * 
     * @throws CamelExecutionException
     * @throws IOException
     */
    @Test
    public void testForSaveOrderNumber() throws CamelExecutionException,
            IOException {

        EntityManager em = Util.getEntityManager();
        SupplierOrderJPA supplierOrder = new SupplierOrderJPA();
        supplierOrder.setInsertDate(new Date());
        supplierOrder.setStatus("new");
        em.getTransaction().begin();
        em.persist(supplierOrder);
        em.getTransaction().commit();
        em.close();

        startEndpoint.sendBodyAndHeader(DIRECT_START_ROUTE,
                fileToString(getClass(), INPUT_SAVE_ORDERNUMBER_MESSAGE),
                "type", "saveOrderNumber");

        em = Util.getEntityManager();
        supplierOrder = new SupplierOrderJPA();
        supplierOrder = em.find(SupplierOrderJPA.class, 1);
        String orderNumber = supplierOrder.getOrderNumber();

        em.close();

        assertEquals("1", orderNumber);

    }

    /**
     * Simplifies file reading
     * 
     * @param loader
     *            Classloder
     * @param fileLocation
     * @return file content
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation)
            throws IOException {
        return IOUtils.toString(
                loader.getClassLoader().getResource(fileLocation),
                Charset.defaultCharset());
    }
}
