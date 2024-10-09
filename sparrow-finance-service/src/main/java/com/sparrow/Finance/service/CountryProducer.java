package com.sparrow.Finance.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CountryProducer {
	
	 private final KafkaTemplate<String, Object> kafkaTemplate;

	    public CountryProducer(KafkaTemplate<String, Object> kafkaTemplate) {
	        this.kafkaTemplate = kafkaTemplate;
	    }

	    public void sendMessage(String topic, Object message) {
	        kafkaTemplate.send(topic, message);
	    }

}
