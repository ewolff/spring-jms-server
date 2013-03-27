Sample Spring JMS Project
=========================

This project shows how the JMS support in Spring can be used without the using Spring's Dependency injection.

* src/main/java/springjms/JmsServer.java is a POJO that is called from JMS and returns an answer
* src/test/java/springjms/SpringJms.java is a JUnit test. It sets up the environment and tests whether the POJO is actually called.

Concretely the following features of Spring JMS are shown:

* DefaultMessageListenerContainer : A lightweight and scalable container to be called from a JMS system.
* MessageListenerAdapter : Adapts a POJO to the MessageListener interface
* JmsTemplate : Simplifies the JMS API

The setup also supports request-reply. As shown a message is sent with a reply-to header and the return value of the method call is send to exactly that queue.
