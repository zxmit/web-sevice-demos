package com.zxm.activemq.demo1;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zxm on 2016/4/14.
 */
public class JMSProducer {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;

    private static final int SENDNUM = 10;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory;

        Connection connection = null;

        Session session;

        Destination destination;

        MessageProducer messageProducer;

        connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME, JMSProducer.PASSWORD, JMSProducer.BROKERURL);

        try {
            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("HelloWorld");

            messageProducer = session.createProducer(destination);

            sendMessage(session, messageProducer);

            session.commit();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public static void sendMessage(Session session, MessageProducer messageProducer) throws JMSException {
        for (int i=0; i < JMSProducer.SENDNUM; i++) {
            TextMessage message = session.createTextMessage("ActiveMQ 发送消息" + i);
            System.out.println("发送消息：Activemq 发送消息 " + i);
            messageProducer.send(message);
        }
    }
}
