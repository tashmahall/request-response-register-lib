package com.igor.requestresponseregisterlib.configurations;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.StringUtils;



//@Configuration
//@EnableJms
public class QueueSenderConfig {
//    @Value("${spring.activemq.broker-url}")
//    private String brokerUrl;
//
//    @Value("${spring.activemq.user}")
//    private String user;
//
//    @Value("${spring.activemq.password}")
//    private String password;
//    
//    @Bean
//    public ActiveMQConnectionFactory connectionFactory() {
//
//
//        ActiveMQConnectionFactory connectionFactory;
//        ActiveMQQueue queueOrdered = new ActiveMQQueue("log-register");
//        
//        RedeliveryPolicy qp10Seconds = new RedeliveryPolicy();
//        qp10Seconds.setInitialRedeliveryDelay(10000);
//        qp10Seconds.setUseCollisionAvoidance(true);
//        qp10Seconds.setRedeliveryDelay(10000);
//        qp10Seconds.setUseExponentialBackOff(false);
//        qp10Seconds.setMaximumRedeliveries(2);
//        qp10Seconds.setDestination(queueOrdered);
//
//        if ( StringUtils.isEmpty(user) ) {
//        	connectionFactory =new ActiveMQConnectionFactory(brokerUrl);
//        }else {
//        	connectionFactory = new ActiveMQConnectionFactory(user, password, brokerUrl);
//        }
//        RedeliveryPolicyMap rdMap = connectionFactory.getRedeliveryPolicyMap();
//        rdMap.put(queueOrdered, qp10Seconds);
//        connectionFactory.setRedeliveryPolicyMap(rdMap);
//        
//        return connectionFactory;
//    }
//    @Bean
//    public JmsListenerContainerFactory jmsFactoryTopic(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//        factory.setPubSubDomain(true);
//        return factory;
//    }
//    @Bean
//    public JmsTemplate jmsTemplate(@Autowired ConnectionFactory connectionFactory) {
//        return new JmsTemplate(connectionFactory);
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplateTopic(@Autowired  ConnectionFactory connectionFactory) {
//        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//        jmsTemplate.setPubSubDomain( true );
//        return jmsTemplate;
//    }    
}
