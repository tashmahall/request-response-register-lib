package com.igor.requestresponseregisterlib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
@Component
public class ActiveMQMessenger implements QueueMessenger {
	@Autowired 
	private JmsTemplate jmsTemplate;
	@Override
	public void sendMessage(String queue, String message) {
		jmsTemplate.convertAndSend(queue,message);
	}
}
