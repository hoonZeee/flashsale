package com.swe.flashsale.service;

import com.swe.flashsale.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducerService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_NAME = "order-requests";
    
    public String sendOrderRequest(Long userId, Long productId, Integer quantity) {
        String requestId = UUID.randomUUID().toString();
        
        OrderMessage message = OrderMessage.builder()
                .requestId(requestId)
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .requestTime(LocalDateTime.now())
                .status("PENDING")
                .build();
        
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_NAME, requestId, message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Order request sent successfully: requestId={}, offset={}", 
                            requestId, result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send order request: requestId={}", requestId, ex);
                }
            });
            
            return requestId;
        } catch (Exception e) {
            log.error("Error sending order request: requestId={}", requestId, e);
            throw new RuntimeException("Failed to send order request", e);
        }
    }
}
