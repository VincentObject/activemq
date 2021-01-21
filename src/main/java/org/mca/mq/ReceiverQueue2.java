
package org.mca.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * active-mq 消息接收
 */
public class ReceiverQueue2 {

	public static void main(String[] args) throws Exception{

		// 1.获取连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin",
				"admin",
				"tcp://47.105.71.60:61616"
				);
		//object类型需 添加信任
		connectionFactory.setTrustedPackages(
				new ArrayList<String>(
						Arrays.asList(
								new String[]{
										Girl.class.getPackage().getName()
								}
						)
				)
		);

		// 2.获取一个向ActiveMQ的连接
		Connection connection = connectionFactory.createConnection();
		connection.start();//消费者必须开启connection.start()
		// 3.获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 4. 找目的地，获取destination，消费端，也会从这个目的地取消息
		Destination queue = session.createQueue("user");
		// 5.获取消息
		MessageConsumer consumer = session.createConsumer(queue,"age>20");

		consumer.setMessageListener(new MyListener());
//		for(int i=0;;i++) {
//			TextMessage message = (TextMessage)consumer.receive();
//			System.out.println("-----");
//			System.out.println("message2:" + message.getText());
//		//	System.out.println("metadata:" + message);
//	//ActiveMQTextMessage {commandId = 303, responseRequired = false, messageId = ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:1:1:100, originalDestination = null, originalTransactionId = null, producerId = ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:1:1, destination = queue://user, transactionId = TX:ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:100, expiration = 0, timestamp = 1578750760787, arrival = 0, brokerInTime = 1578750760787, brokerOutTime = 1578750981647, correlationId = null, replyTo = null, persistent = true, type = null, priority = 4, groupID = null, groupSequence = 0, targetConsumerId = null, compressed = false, userID = null, content = null, marshalledProperties = null, dataStructure = null, redeliveryCounter = 2, size = 0, properties = null, readOnlyProperties = true, readOnlyBody = true, droppable = false, jmsXGroupFirstForConsumer = false, text = hi: 99}
//			// 事务中的一个操作
//		//	message.acknowledge();
//			// commit()
//			if(i % 2 == 0)
//				message.acknowledge();
//		}
	}
	/*
	 * 还有个问题就是receiver端createsession时是true，后面的参数为啥不会被覆盖？
	 * 
	 * 老师有点不理解，你未确认，另外一个消费者不重复消费？但你再重开消费者一个就能消费
	 */
	
}
