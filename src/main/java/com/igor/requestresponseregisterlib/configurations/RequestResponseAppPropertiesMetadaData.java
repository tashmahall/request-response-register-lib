package com.igor.requestresponseregisterlib.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="com.igor.requestresponseregisterlib")
public class RequestResponseAppPropertiesMetadaData {
	private QueueName queueName;
	
	public QueueName getQueueName() {
		return queueName;
	}

	public void setQueueName(QueueName queueName) {
		this.queueName = queueName;
	}

	public static class QueueName{
		private String responseQueue;
		private String requestQueue;
		public String getResponseQueue() {
			return responseQueue;
		}
		public void setResponseQueue(String responseQueue) {
			this.responseQueue = responseQueue;
		}
		public String getRequestQueue() {
			return requestQueue;
		}
		public void setRequestQueue(String requestQueue) {
			this.requestQueue = requestQueue;
		}
	}

}
