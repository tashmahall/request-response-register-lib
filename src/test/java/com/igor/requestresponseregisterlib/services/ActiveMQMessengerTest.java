package com.igor.requestresponseregisterlib.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

@ExtendWith(MockitoExtension.class) 
public class ActiveMQMessengerTest {
	@InjectMocks
	private ActiveMQMessenger messenger;
	@Mock
	private JmsTemplate jmsTemplate;
	
	@Test
	public void testSendMessage() {
		doNothing().when(jmsTemplate).convertAndSend("queue","message");
		messenger.sendMessage("queue","message");
		verify(jmsTemplate,times(1)).convertAndSend("queue","message");
	}

}
