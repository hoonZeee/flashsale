package com.swe.flashsale.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "orders-topic", groupId = "flashsale-group")
    public void consume(OrderRequest req) {
        try {
            Long orderId = orderService.placeOrderCore(req);
            orderService.callPaymentAndLog(orderId);
            System.out.println("주문 처리 완료: " + orderId);
        } catch (Exception e) {
            System.err.println("주문 처리 실패: " + e.getMessage());
        }
    }
}
