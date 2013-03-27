package springjms;

import static org.junit.Assert.assertEquals;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

public class SpringJms {

	private ActiveMQConnectionFactory activeMQConnectionFactory;

	private DefaultMessageListenerContainer messageListenerContainer;

	private JmsTemplate jmsTemplate;

	@Before
	public void setUp() {
		Destination destination = new ActiveMQQueue("jms-queue");
		activeMQConnectionFactory = new ActiveMQConnectionFactory(
				"vm://localhost");
		messageListenerContainer = defaultMessageListenerContainer(
				activeMQConnectionFactory, destination);
		jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
	}

	private DefaultMessageListenerContainer defaultMessageListenerContainer(
			ActiveMQConnectionFactory activeMQConnectionFactory,
			Destination destination) {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer
				.setConnectionFactory(activeMQConnectionFactory);
		defaultMessageListenerContainer.setDestinationName("jms-queue");
		defaultMessageListenerContainer
				.setMessageListener(messageListenerAdapter());
		defaultMessageListenerContainer.setDestination(destination);
		defaultMessageListenerContainer.afterPropertiesSet();
		defaultMessageListenerContainer.start();
		return defaultMessageListenerContainer;
	}

	public MessageListenerAdapter messageListenerAdapter() {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(
				new JmsServer());
		messageListenerAdapter.setDefaultListenerMethod("handleMessage");
		return messageListenerAdapter;
	}

	@Test(timeout = 2500)
	public void checkResponse() throws Exception {
		final Queue replyQueue = new ActiveMQQueue("MyTempQueue");
		jmsTemplate.convertAndSend("jms-queue", "Eberhard",
				new MessagePostProcessor() {

					@Override
					public Message postProcessMessage(Message message)
							throws JMSException {
						message.setJMSReplyTo(replyQueue);
						return message;
					}
				});
		assertEquals("Hello Eberhard",
				jmsTemplate.receiveAndConvert(replyQueue));
	}

}
