package com.swe.flashsale.messaging;

import com.swe.flashsale.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;
    private static final String TOPIC = "flashsale-orders";

    public void send(OrderMessage message) {

        kafkaTemplate.send(TOPIC, message.getTraceId(), message);
        log.debug("[Kafka] sent: topic={}, key={}, userId={}, productId={}, qty={}",
                TOPIC, message.getTraceId(), message.getUserId(), message.getProductId(), message.getQuantity());
    }
}
