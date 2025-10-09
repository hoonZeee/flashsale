package com.swe.flashsale.service;

import com.swe.flashsale.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducerService {

    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    public void sendOrderRequest(OrderMessage message) {
        log.info("Sending order request to Kafka: {}", message);
        kafkaTemplate.send("order-requests", message.getRequestId(), message);
    }
}
