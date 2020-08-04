package com.igor.requestresponseregisterlib.services;


import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.igor.requestresponseregisterlib.annotations.ResponseRegister;

@ControllerAdvice
@Component
public class RegisterResponseTraceService implements ResponseBodyAdvice<Object> {
	@Autowired 
	private QueueMessenger jmsTemplate;
	private Logger logger = LogManager.getLogger(this.getClass());
	@Value("${com.igor.requestresponseregisterlib.queue-name.response-queue}")
	private String queueName;

    private void recordResponse(Object body) {
    	logger.debug("Queued message [%s]",body == null ? null:body.toString());
		jmsTemplate.sendMessage(queueName,body == null ? null:body.toString());
    }
    
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		if(methodParameter.getAnnotatedElement().isAnnotationPresent(ResponseRegister.class)) 
			return true;
		return false;
	}
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, @Nonnull ServerHttpResponse response) {
		logger.debug("Response Body [%s]",body == null ? null:body.toString());
		recordResponse(getObjectNode(response.getHeaders(),body));
		return body;
	}
    private ObjectNode getObjectNode(@Nonnull HttpHeaders headers, Object arg0) {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.registerModule(new JavaTimeModule());
    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    	ObjectNode headerNode = objectMapper.createObjectNode();
		headers.entrySet().forEach(entry ->
			entry.getValue().forEach(value ->{
				headerNode.put(entry.getKey(), value);
			})
		);
		logger.debug("Response Header [%s]",headerNode == null ? null:headerNode.toString()); 
		ObjectNode messageNode = objectMapper.createObjectNode();
		messageNode.set("header", headerNode);
	    String body = StringUtils.isEmpty(arg0)?null:arg0.toString();
		messageNode.put("body", body);
		return messageNode;
    }
}
