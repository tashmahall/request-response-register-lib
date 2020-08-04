package com.igor.requestresponseregisterlib.services;

import java.lang.reflect.Type;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.igor.requestresponseregisterlib.annotations.RequestRegister;

@ControllerAdvice
@Component
public  class RegisterRequestTraceService extends RequestBodyAdviceAdapter implements HandlerInterceptor  {
	@Autowired 
	private QueueMessenger jmsTemplate;
	
	private Logger logger = LogManager.getLogger(RegisterRequestTraceService.class);
	@Value("${com.igor.requestresponseregisterlib.queue-name.request-queue}")
	private String queueName;
	
    private void recordRequest(Object body) {
    	logger.debug("Queued message ["+body.toString()+"]");
		jmsTemplate.sendMessage(queueName,body.toString());
    }
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		if(methodParameter.getAnnotatedElement().isAnnotationPresent(RequestRegister.class)) 
			return true;
		return false;
	}

	@Override
	public Object afterBodyRead(Object arg0, HttpInputMessage inputMessage, MethodParameter arg2, Type arg3, Class<? extends HttpMessageConverter<?>> arg4) {
		logger.debug("Request Body ["+arg0+"]");
		recordRequest (getObjectNode(inputMessage.getHeaders(), arg0));
		return arg0;
	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && request.getMethod().equals(HttpMethod.GET.name())) {
        	
        	recordRequest(request);
        }
        
        return true;
    }
    private ObjectNode getObjectNode(HttpHeaders headers, Object arg0) {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.registerModule(new JavaTimeModule());
    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    	ObjectNode headerNode = objectMapper.createObjectNode();
		headers.entrySet().forEach(entry ->
			entry.getValue().forEach(value ->{
				headerNode.put(entry.getKey(), value);
			})
		);
		logger.debug("Request Header ["+headerNode.toString()+"]");
		ObjectNode messageNode = objectMapper.createObjectNode();
		try {
			messageNode.put("request date", objectMapper.writeValueAsString(new Date()));
			messageNode.set("header", headerNode);
		    String body = StringUtils.isEmpty(arg0)?null:arg0.toString();
			messageNode.put("body", body);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return messageNode;
    }
}
