package au.com.marlo.trainning.activemq.producer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import au.com.marlo.trainning.activemq.producer.test.util.Subscriber;
import au.com.marlo.trainning.activemq.producer.Publisher;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

//Test Class to check if PublisherTest class publishes a message in a Topic
public class PublisherTest {

    private static Publisher publisherPublishSubscribe,
            publisherMultipleConsumers;
    private static Subscriber subscriberPublishSubscribe,
            subscriber1MultipleConsumers, subscriber2MultipleConsumers;

    // Creates all objects to setup the test
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        Connection connectionPublisher1 = connectionFactory.createConnection();
        Connection connectionPublisher2 = connectionFactory.createConnection();

        Connection connectionSubscriber1 = connectionFactory.createConnection();
        Connection connectionSubscriber2 = connectionFactory.createConnection();
        Connection connectionSubscriber3 = connectionFactory.createConnection();

        publisherPublishSubscribe = new Publisher();
        publisherPublishSubscribe.create(connectionPublisher1,"publisher-topic",
                "topic");

        publisherMultipleConsumers = new Publisher();
        publisherMultipleConsumers.create(connectionPublisher2,"publisher-topicMultipleConsumers",
                "topicMultipleConsumers");

        subscriberPublishSubscribe = new Subscriber();
        subscriberPublishSubscribe.create(connectionSubscriber1,"singleSubscriber","topic","subscribe");

        subscriber1MultipleConsumers = new Subscriber();
        subscriber1MultipleConsumers.create(connectionSubscriber2,"multipleSubscriber1","topicMultipleConsumers","subscribe1");

        subscriber2MultipleConsumers = new Subscriber();
        subscriber2MultipleConsumers.create(connectionSubscriber3,"multipleSubscriber2","topicMultipleConsumers","subscribe2");

    }


    //Close all the connections
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        publisherPublishSubscribe.closeConnection();
        publisherMultipleConsumers.closeConnection();

        subscriberPublishSubscribe.removeSubscriber();
        subscriber1MultipleConsumers.removeSubscriber();
        subscriber2MultipleConsumers.removeSubscriber();

        subscriberPublishSubscribe.closeConnection();
        subscriber1MultipleConsumers.closeConnection();
        subscriber2MultipleConsumers.closeConnection();

    }


    /*
    ** Test if a publisher sends one specific message to a Topic
    */
    @Test
    public void testGetGreeting() {
        try {
            publisherPublishSubscribe.send("Test Message 1");

            String greeting1 = subscriberPublishSubscribe.getGreeting(1000);
            assertTrue(greeting1.contains("Test Message 1"));

            String greeting2 = subscriberPublishSubscribe.getGreeting(1000);
            assertEquals("no greeting", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    /*
    ** Test if a publisher sends  messages to all Subscribers
    */

    @Test
    public void testMultipleConsumers() {
        try {
            publisherMultipleConsumers.send("Test Message 2");

            String greeting1 = subscriber1MultipleConsumers.getGreeting(1000);
            assertTrue(greeting1.contains("Test Message 2"));

            String greeting2 = subscriber2MultipleConsumers.getGreeting(1000);
            assertTrue(greeting2.contains("Test Message 2") );

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

}