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
            // 주문 처리 (재고 차감 + 주문 저장)
            Long orderId = orderService.placeOrderCore(
                    message.getUserId(), 
                    message.getProductId(), 
                    message.getQuantity()
            );
            
            // 결제 처리 (비동기)
            orderService.callPaymentAndLog(orderId);
            
            log.info("Order processed successfully: requestId={}, orderId={}", 
                    message.getRequestId(), orderId);
            
            // 수동 커밋
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Failed to process order request: requestId={}", 
                    message.getRequestId(), e);
            
            // 에러가 발생해도 커밋하여 재처리 방지
            acknowledgment.acknowledge();
        }
    }
}
