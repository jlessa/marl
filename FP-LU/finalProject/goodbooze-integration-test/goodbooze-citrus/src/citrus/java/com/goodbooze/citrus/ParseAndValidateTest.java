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
public class ParseAndValidateTest extends TestNGCitrusTestBuilder {
    @Autowired
    @Qualifier("myDataSource")
    private DataSource myDataSource;

    // Sending message to fuse
    ClassPathResource InitialSOAPMessageSend = new ClassPathResource(
            "ParseAndValidateTest/messages/test1-soap-message.xml");
    ClassPathResource InitialSOAPMessageReceive = new ClassPathResource(
            "commonmessages/soap-message-receive.xml");

    // Receiving message from wildfly
    // supplierA
    ClassPathResource restMessageReceive = new ClassPathResource(
            "ParseAndValidateTest/messages/rest-message-receive.json");
    // supplierB
    ClassPathResource soapMessageReceiveSupplierB = new ClassPathResource(
            "ParseAndValidateTest/messages/soap-message-supplierb-receive.xml");

    // supplierC
    ClassPathResource jmsMessageReceiveSupplierC = new ClassPathResource(
            "ParseAndValidateTest/messages/jms-message-supplierc-receive.xml");

    @CitrusTest(name = "ParseAndValidateTest")
    public void testFirstStep() {
        echo("send SOAP request message");
        send("soapClient").payload(InitialSOAPMessageSend);
        receive("soapClient").payload(InitialSOAPMessageReceive);

        parallel(
                sequential(echo("Receive REST request message"),
                        receive("RestService").messageType(MessageType.JSON)
                                .payload(restMessageReceive)),
                sequential(
                        echo("Receive SOAP request message to queue destination"),
                        receive("SOAPService").payload(
                                soapMessageReceiveSupplierB)),
                sequential(
                        echo("Receive JMS request message to queue destination"),
                        receive("supplierCService").extractFromHeader(
                                "citrus_jms_correlationId", "myCorrelationId")
                                .payload(jmsMessageReceiveSupplierC)));
        
        sleep(1.0);
        
        query(myDataSource)
                .statement(
                        "SELECT orderNumber as ONR1 FROM supplierorder where id = 1")
                .statement(
                        "SELECT orderNumber as ONR2 FROM supplierorder where id = 2")
                .statement(
                        "SELECT orderNumber as ONR3 FROM supplierorder where id = 3")
                .validate("ONR1", "").validate("ONR2", "").validate("ONR3", "");
    }
}