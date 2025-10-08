package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/order-direct")
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequest req) {
        Long orderId = orderService.placeOrderCore(req);  // 재고락+주문 저장
        orderService.callPaymentAndLog(orderId);          // 외부결제 모의 호출
        return ResponseEntity.ok("OK: orderId=" + orderId);
    }


    @PostMapping("/order-kafka")
    public ResponseEntity<String> placeOrderAsync(@RequestBody @Valid OrderRequest req) {

        // 주문 요청을 Kafka의 order-topic 토픽으로 메시지 전송
        kafkaTemplate.send("order-topic", req);

        // 결제 작업 완료를 기다리지 않고 즉시 응답
        return ResponseEntity.ok("ACK: 주문 요청이 Kafka에 접수되었습니다.");
    }
}