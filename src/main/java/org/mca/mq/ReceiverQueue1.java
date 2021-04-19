
package org.mca.mq;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * active-mq 消息接收
 */

/**
 * 独占消费者
 *  Queue queue = session.createQueue("xxoo?consumer.exclusive=true");
 */
public class ReceiverQueue1 {

	public static void main(String[] args) throws Exception{

		// 1.获取连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin",
				"admin",
				"tcp://47.105.71.60:5671"
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
		for(int i=0;;i++) {
			TextMessage message = (TextMessage)consumer.receive();
			System.out.println("-----");
			System.out.println("message2:" + message.getText());
		//	System.out.println("metadata:" + message);
	//ActiveMQTextMessage {commandId = 303, responseRequired = false, messageId = ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:1:1:100, originalDestination = null, originalTransactionId = null, producerId = ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:1:1, destination = queue://user, transactionId = TX:ID:DESKTOP-OEK4RCQ-12451-1578750463269-1:1:100, expiration = 0, timestamp = 1578750760787, arrival = 0, brokerInTime = 1578750760787, brokerOutTime = 1578750981647, correlationId = null, replyTo = null, persistent = true, type = null, priority = 4, groupID = null, groupSequence = 0, targetConsumerId = null, compressed = false, userID = null, content = null, marshalledProperties = null, dataStructure = null, redeliveryCounter = 2, size = 0, properties = null, readOnlyProperties = true, readOnlyBody = true, droppable = false, jmsXGroupFirstForConsumer = false, text = hi: 99}
			// 事务中的一个操作
		//	message.acknowledge();
			// commit()
//			if(i % 2 == 0)
//				message.acknowledge();
		}
	}

	/**
	 * **如果遇到此类报错**
	 * Exception in thread "main" javax.jms.JMSException: Failed to build body from content. Serializable class not available to broker. Reason: java.lang.ClassNotFoundException: Forbidden class com.mashibing.mq.Girl! This class is not trusted to be serialized as ObjectMessage payload. Please take a look at http://activemq.apache.org/objectmessage.html for more information on how to configure trusted classes.
	 * 	at org.apache.activemq.util.JMSExceptionSupport.create(JMSExceptionSupport.java:36)
	 * 	at org.apache.activemq.command.ActiveMQObjectMessage.getObject(ActiveMQObjectMessage.java:213)
	 * 	at com.mashibing.mq.Receiver.main(Receiver.java:65)
	 * Caused by: java.lang.ClassNotFoundException: Forbidden class com.mashibing.mq.Girl! This class is not trusted to be serialized as ObjectMessage payload. Please take a look at http://activemq.apache.org/objectmessage.html for more information on how to configure trusted classes.
	 * 	at org.apache.activemq.util.ClassLoadingAwareObjectInputStream.checkSecurity(ClassLoadingAwareObjectInputStream.java:112)
	 * 	at org.apache.activemq.util.ClassLoadingAwareObjectInputStream.resolveClass(ClassLoadingAwareObjectInputStream.java:57)
	 * 	at java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1868)
	 * 	at java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1751)
	 * 	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2042)
	 * 	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1573)
	 * 	at java.io.ObjectInputStream.readObject(ObjectInputStream.java:431)
	 * 	at org.apache.activemq.command.ActiveMQObjectMessage.getObject(ActiveMQObjectMessage.java:211)
	 *
	 * 	需要添加信任
	 * 			connectionFactory.setTrustedPackages(
	 * 				new ArrayList<String>(
	 * 						Arrays.asList(
	 * 								new String[]{
	 * 										Girl.class.getPackage().getName()
	 *                                                                                }
	 * 								)
	 * 						)
	 * 				);
	 */
	
}
