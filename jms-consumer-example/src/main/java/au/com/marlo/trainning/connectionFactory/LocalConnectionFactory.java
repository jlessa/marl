package au.com.marlo.trainning.connectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;


public class LocalConnectionFactory {
    public static Connection createConnection () throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        return connectionFactory.createConnection();
    }
}
