package org.mca.mq.replyto;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.mca.mq.Girl;
import org.mca.mq.MyListener;

import javax.jms.*;

/**
 * active-mq replyTo  生产者发送消息后 设置一个replyto队列， 消费者拿到消息，从消息中获取生产者设置的replyto队列，发送回复消息，生产者作为消费者再次消费回复的消息
 */
public class SenderQueueReplyTo {

    public static void main(String[] args) throws Exception {

        // 1.获取连接工厂

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://47.105.71.60:5671"
        );
        // 2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        // 3.获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4. 找目的地，获取destination，消费端，也会从这个目的地取消息
        Queue queue = session.createQueue("user");
        // 51.消息创建者
        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("消息test");
        textMessage.setJMSReplyTo( session.createQueue("tmp"));
        producer.send(textMessage);
        System.out.println("System exit....");

        connection.start();
        TextMessage tmp = (TextMessage)session.createConsumer(session.createQueue("tmp")).receive();
        System.out.println("receive:"+tmp.getText());

        // 6.关闭连接
        connection.close();
    }


}
