package org.mca.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * active-mq 消息发送
 */


/**
 *
 * 持久化默认是开启的
 * producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT) 非持久化
 *
 * KahaDB是默认的持久化策略
 * <persistenceAdapter>
 *     <!--directory:保存数据的目录;journalMaxFileLength:保存消息的文件大小 --> <kahaDBdirectory="${activemq.data}/kahadb"journalMaxFileLength="16mb"/>
 * </persistenceAdapter>
 *
 *
 * JDBC存储
 * **Beans中添加**
 * <bean id="mysql-ds" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
 *      <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
 *      <property name="url" value="jdbc:mysql://localhost/activemq?relaxAutoCommit=true"/>
 *      <property name="username" value="activemq"/>
 *      <property name="password" value="activemq"/>
 *      <property name="maxActive" value="200"/>
 *      <property name="poolPreparedStatements" value="true"/>
 * </bean>
 * **修改persistenceAdapter**
 * <persistenceAdapter>
 *      <!-- <kahaDB directory="${activemq.data}/kahadb"/> -->
 * 		<jdbcPersistenceAdapter dataSource="#mysql-ds" createTablesOnStartup="true" />
 *</persistenceAdapter>
 *
 *
 *可以设置消费优先级
 * producer.setPriority
 *配置文件需要指定使用优先级的目的地
 *<policyEntry queue="queue1" prioritizedMessages="true" />
 *
 *
 *消息超时/过期
 * producer.setTimeToLive
 *
 * 私信队列 默认是开启状态，可以通过配置文件关闭私信队列，私信队列可以设置消费者去消费，非持久话的数据不会进入私信队列，如果非持久话的数据想要进入私信队列
 * 需要进行配置文件修改。
 * 让非持久化的消息也进入死信队列
 *<individualDeadLetterStrategy   queuePrefix="DLxxQ." useQueueForQueueMessages="true"  processNonPersistent="true" />
 * 过期消息不进死信队列
 * <individualDeadLetterStrategy   processExpired="false"  />
 *
 *
 *
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
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //设置非持久化, 默认情况下非持久化的数据不会进入私信队列，如果需要进入，配置文件可进行调整
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
