
package org.mca.mq.replyto;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * active-mq 消息接收
 */
public class ReceiverQueueReplyTo {

    public static void main(String[] args) throws Exception {

        // 1.获取连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://47.105.71.60:5671"
        );
        // 2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        connection.start();//消费者必须开启connection.start()
        // 3.获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4. 找目的地，获取destination，消费端，也会从这个目的地取消息
        Destination queue = session.createQueue("user");
        // 5.获取消息
        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println("-----" + message.getText());
        Destination jmsReplyTo = message.getJMSReplyTo();
        session.createProducer(jmsReplyTo).send( session.createTextMessage("回复"));
    }
}
