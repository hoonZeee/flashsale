package com.swe.flashsale.service;

import com.swe.flashsale.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumerService {

    private final OrderService orderService;

    @KafkaListener(topics = "order-requests", groupId = "flashsale-group")
    public void processOrderRequest(
            @Payload OrderMessage message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        log.info("Processing order request: requestId={}, partition={}, offset={}",
                message.getRequestId(), partition, offset);

        try {
            Long orderId = orderService.placeOrderCore(message.getUserId(), message.getProductId(), message.getQuantity());
            orderService.callPaymentAndLog(orderId);
            acknowledgment.acknowledge();
            log.info("Order request processed successfully: requestId={}, orderId={}", message.getRequestId(), orderId);
        } catch (Exception e) {
            log.error("Error processing order request: requestId={}, error={}", message.getRequestId(), e.getMessage());
            // TODO: Implement dead-letter queue or retry mechanism
        }
    }
}
