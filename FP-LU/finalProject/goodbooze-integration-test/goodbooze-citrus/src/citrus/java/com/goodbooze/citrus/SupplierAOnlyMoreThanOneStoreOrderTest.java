package com.goodbooze.citrus;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import com.consol.citrus.dsl.TestNGCitrusTestBuilder;
import com.consol.citrus.dsl.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;

/**
 * This is a Citrus integration test for createOrder using SOAP client.
 * 
 */
@Test
public class SupplierAOnlyMoreThanOneStoreOrderTest extends
        TestNGCitrusTestBuilder {
    @Autowired
    @Qualifier("myDataSource")
    private DataSource myDataSource;

    // Sending message to fuse
    ClassPathResource InitialSOAPMessageSend = new ClassPathResource(
            "SupplierAOnlyMoreThanOneStoreOrderTest/messages/test1-soap-message.xml");
    ClassPathResource InitialSOAPMessageReceive = new ClassPathResource(
            "commonmessages/soap-message-receive.xml");

    // Receiving message from wildfly
    // supplierA
    ClassPathResource restMessageReceive = new ClassPathResource(
            "SupplierAOnlyMoreThanOneStoreOrderTest/messages/rest-message-receive.json");
   

    @CitrusTest(name = "SupplierAOnlyMoreThanOneStoreOrderTest")
    public void testFirstStep() {
        echo("send SOAP request message");
        send("soapClient").payload(InitialSOAPMessageSend);
        receive("soapClient").payload(InitialSOAPMessageReceive);

        echo("Receive REST request message");
        receive("RestService").messageType(MessageType.JSON)
            .payload(restMessageReceive);
        send("RestService")
            .payload("{orderNumber: 3333}");
        
        sleep(1.0);
        
     // query validations.
        query(myDataSource)
                .statement("SELECT COUNT(*) as COUNTENTRY FROM entry")
                .statement(
                        "SELECT COUNT(*) as COUNTSO FROM storeorder")
                .statement(
                        "SELECT entryId as SOENTRYID  FROM storeorder")       
                .statement(
                        "SELECT COUNT(*) as COUNTSUPPORDER FROM supplierorder where SUPPLIERJPA_ID = 1")            
                .statement(
                        "SELECT COUNT(*) as COUNTISO FROM itemstoreorder")
                .statement(
                        "SELECT COUNT(*) as COUNTISUPPO FROM itemsupplierorder")       
                .statement(
                        "SELECT quantity as QUANTITYISOP1 FROM itemstoreorder WHERE productInternalId = 1 ORDER BY quantity")
                .statement(
                        "SELECT quantity as QUANTITYISOP2 FROM itemstoreorder WHERE productInternalId = 2 ORDER BY quantity")  
                .statement(
                        "SELECT quantity as QUANTITYISOP6 FROM itemstoreorder WHERE productInternalId = 6 ORDER BY quantity")
                .statement(
                        "SELECT quantity as QUANTITYISUPPO FROM itemsupplierorder ORDER BY quantity")          
                .statement(
                        "SELECT orderNumber as ONR1 FROM supplierorder where SUPPLIERJPA_ID = 1")
                               

                .validate("COUNTENTRY", "1")              
                .validate("COUNTSO", "3")
                .validate("COUNTSUPPORDER", "1")
                .validate("SOENTRYID", "1", "1", "1")
                .validate("COUNTISO", "5")
                .validate("COUNTISUPPO", "3")
                .validate("QUANTITYISOP1", "100", "300")
                .validate("QUANTITYISOP2", "600", "700")
                .validate("QUANTITYISOP6", "500")
                .validate("QUANTITYISUPPO", "400", "500", "1300")
                .validate("ONR1", "3333");
    }
}
