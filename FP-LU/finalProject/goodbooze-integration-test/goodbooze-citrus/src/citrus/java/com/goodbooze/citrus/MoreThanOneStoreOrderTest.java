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
public class MoreThanOneStoreOrderTest extends TestNGCitrusTestBuilder {
    @Autowired
    @Qualifier("myDataSource")
    private DataSource myDataSource;
    
    
    //Sending message to fuse
    ClassPathResource InitialSOAPMessageSend = new ClassPathResource(
            "MoreThanOneStoreOrderTest/messages/test1-soap-message.xml");
    ClassPathResource InitialSOAPMessageReceive = new ClassPathResource(
            "commonmessages/soap-message-receive.xml");
    
    //Receiving message from  wildfly
    //supplierA
    ClassPathResource restMessageReceive = new ClassPathResource(   
            "MoreThanOneStoreOrderTest/messages/rest-message-receive.json");
    //supplierB
    ClassPathResource soapMessageReceiveSupplierB = new ClassPathResource(
            "MoreThanOneStoreOrderTest/messages/soap-message-supplierb-receive.xml");
    
    ClassPathResource soapMessageResponseSupplierB = new ClassPathResource(
            "MoreThanOneStoreOrderTest/messages/soap-message-supplierb-response.xml");
    //supplierC
    ClassPathResource jmsMessageReceiveSupplierC = new ClassPathResource(
            "MoreThanOneStoreOrderTest/messages/jms-message-supplierc-receive.xml");
    
    ClassPathResource jmsMessageResponseSupplierC = new ClassPathResource(
            "MoreThanOneStoreOrderTest/messages/jms-message-supplierc-response.xml");

    @CitrusTest(name = "MoreThanOneStoreOrderTest")
    public void testFirstStep() {
        echo("send SOAP request message");
        send("soapClient").payload(InitialSOAPMessageSend);
        receive("soapClient").payload(InitialSOAPMessageReceive);

        parallel(
                sequential(echo("Receive REST request message"),
                        receive("RestService").messageType(MessageType.JSON)
                                .payload(restMessageReceive),
                        send("RestService").payload("{orderNumber: 2222}")),
                sequential(
                        echo("Receive SOAP request message to queue destination"),
                        receive("SOAPService").payload(
                                soapMessageReceiveSupplierB),
                        send("SOAPService").payload(
                                soapMessageResponseSupplierB)),
                sequential(
                        echo("Receive JMS request message to queue destination"),
                        receive("supplierCService").extractFromHeader(
                                "citrus_jms_correlationId", "myCorrelationId")
                                .payload(jmsMessageReceiveSupplierC),
                        send("supplierCService").header(
                                "citrus_jms_correlationId",
                                "${myCorrelationId}").payload(
                                jmsMessageResponseSupplierC)));
        
        sleep(1.0);
        query(myDataSource)
        .statement("SELECT COUNT(*) as COUNTENTRY FROM entry")
        .statement(
                "SELECT COUNT(*) as COUNTSO FROM storeorder")
        .statement(
                "SELECT COUNT(*) as COUNTISO FROM itemstoreorder")
        .statement(
                "SELECT quantity as QUANTITYISOP1 FROM itemstoreorder WHERE productInternalId = 1 ORDER BY quantity" )
        .statement(
                "SELECT quantity as QUANTITYISOP3 FROM itemstoreorder WHERE productInternalId = 3 ORDER BY quantity")  
        .statement(
                "SELECT quantity as QUANTITYISOP5 FROM itemstoreorder WHERE productInternalId = 5 ORDER BY quantity")           
        .statement(
                "SELECT orderNumber as ONR1 FROM supplierorder where SUPPLIERJPA_ID = 1")
        .statement(
                "SELECT orderNumber as ONR2 FROM supplierorder where SUPPLIERJPA_ID = 2")
        .statement(
                "SELECT orderNumber as ONR3 FROM supplierorder where SUPPLIERJPA_ID = 3")
        .statement(
                "SELECT quantity as QUANTITIESIITEMSUP FROM itemsupplierorder ORDER BY quantity" )
         
       
        .validate("COUNTENTRY", "1")
        .validate("COUNTSO", "3")
        .validate("COUNTISO", "7")
        .validate("QUANTITYISOP1", "100", "400", "600")
        .validate("QUANTITYISOP3", "200", "700")
        .validate("QUANTITYISOP5", "300", "500")
        .validate("ONR1", "2222")
        .validate("ONR2", "20002")
        .validate("ONR3", "2222222")
        .validate("QUANTITIESIITEMSUP", "800", "900", "1100");
    }
}
