package org.mca.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

/**
 * active-mq 消息发送
 */
public class SenderQueueDelay {

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
//        for (int i = 0; i < 10; i++) {
//            TextMessage textMessage = session.createTextMessage("hi: " + i);
            // 5.3 向目的地写入消息
//            if(i % 4 == 0) {
//                // 设置消息的优先级
//                // 对producer 整体设置
//                //	producer.setPriority(9);
//                //	producer.send(textMessage,DeliveryMode.PERSISTENT,9,1000 * 100);
//                textMessage.setJMSPriority(9);
//            }
            ObjectMessage objectMessage = session.createObjectMessage(new Girl("张三", 18, 100.0));
            long delay = 10 * 1000;
            long period = 2 * 1000;
            int repeat = 9;
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            objectMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
        objectMessage.setIntProperty("age",10);

        ObjectMessage objectMessage2 = session.createObjectMessage(new Girl("李四", 30, 200.0));
        long delay2 = 10 * 1000;
        long period2 = 2 * 1000;
        int repeat2 = 9;
        objectMessage2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay2);
        objectMessage2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period2);
        objectMessage2.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat2);
        objectMessage2.setIntProperty("age",30);
//            MapMessage mapMessage = session.createMapMessage();
//            mapMessage.setString("map测试","success");

//            producer.send(textMessage);
            producer.send(objectMessage);
        producer.send(objectMessage2);
//            producer.send(mapMessage);
            //	Thread.sleep(3000);
//            if(i%10==0){
//                textMessage.acknowledge();
//            }
//        }

        // 6.关闭连接
        connection.close();
        System.out.println("System exit....");
    }


}
