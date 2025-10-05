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
private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/order-direct")
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequest req) {
        Long orderId = orderService.placeOrderCore(req);  // 재고락+주문 저장
        orderService.callPaymentAndLog(orderId);          // 외부결제 모의 호출
        return ResponseEntity.ok("OK: orderId=" + orderId);
    }

// ✅ Kafka 방식 추가
   @PostMapping("/order-kafka")
public ResponseEntity<String> placeOrderKafka(@RequestBody @Valid OrderRequest req) {
    String msg = "{ \"userId\": " + req.getUserId()
               + ", \"productId\": " + req.getProductId()
               + ", \"quantity\": " + req.getQuantity() + " }";

    kafkaTemplate.send("orders", msg);
    return ResponseEntity.ok("OK: request sent to Kafka");
}

}