package au.com.marlo.trainning.activemq.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import au.com.marlo.trainning.activemq.consumer.Subscriber;
import au.com.marlo.trainning.activemq.test.util.Publisher;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

 /*
 ** Class to test the Subscriber class
 */

public class SubscriberTest {

    private static Publisher publisherPublishSubscribe,
            publisherMultipleConsumers;
    private static Subscriber subscriberPublishSubscribe,
            subscriber1MultipleConsumers, subscriber2MultipleConsumers;

    /**
     * Creates all objects to setup the test
     * @throws Exception
     */

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

    /**
     * Close all the connections
     */

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
    ** Test if a subscriber receive one specific message from a Topic
    */
    @Test
    public void testGetGreeting() {
        try {
            publisherPublishSubscribe.send("Test Message 1");

            String greeting1 = subscriberPublishSubscribe.getGreeting(1000);
            assertEquals("Test Message 1", greeting1);

            String greeting2 = subscriberPublishSubscribe.getGreeting(1000);
            assertEquals("no greeting", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    /*
    ** Test if the subscribers receive the message from a subscribed Topic
    */
    @Test
    public void testMultipleConsumers() {
        try {
            publisherMultipleConsumers.send("Test Message 2");

            String greeting1 = subscriber1MultipleConsumers.getGreeting(1000);
            assertEquals("Test Message 2", greeting1);

            String greeting2 = subscriber2MultipleConsumers.getGreeting(1000);
            assertEquals("Test Message 2", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

}