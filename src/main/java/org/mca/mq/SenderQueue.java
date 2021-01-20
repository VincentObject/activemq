package org.mca.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * active-mq 消息发送
 */
public class SenderQueue {

    public static void main(String[] args) throws Exception{

        // 1.获取连接工厂

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://47.105.71.60:61616"
        );
        // 2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        // 3.获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4. 找目的地，获取destination，消费端，也会从这个目的地取消息
        Queue queue = session.createQueue("user");
        // 51.消息创建者
        MessageProducer producer = session.createProducer(queue);
        //	producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        // consumer -> 消费者
        // producer -> 创建者
        // 5.2. 创建消息
//        producer.setTimeToLive(1000); //这是进入私信队列时间
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //设置非持久化
        for (int i = 0; i < 10; i++) {
            TextMessage textMessage = session.createTextMessage("hi: " + i);
            // 5.3 向目的地写入消息
//            if(i % 4 == 0) {
//                // 设置消息的优先级
//                // 对producer 整体设置
//                //	producer.setPriority(9);
//                //	producer.send(textMessage,DeliveryMode.PERSISTENT,9,1000 * 100);
//                textMessage.setJMSPriority(9);
//            }
            ObjectMessage objectMessage = session.createObjectMessage(new Girl("张三", 18, 100.0));
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("map测试","success");

            producer.send(textMessage);
            producer.send(objectMessage);
            producer.send(mapMessage);
            //	Thread.sleep(3000);
//            if(i%10==0){
//                textMessage.acknowledge();
//            }
        }

        // 6.关闭连接
        connection.close();
        System.out.println("System exit....");
    }


}
