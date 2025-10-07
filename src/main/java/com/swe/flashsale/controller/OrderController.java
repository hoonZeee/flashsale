package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderMessage;
import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.messaging.OrderProducer;
import com.swe.flashsale.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderProducer orderProducer;

    @PostMapping("/order-direct")
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequest req) {
        Long orderId = orderService.placeOrderCore(req);  // 재고락+주문 저장
        orderService.callPaymentAndLog(orderId);          // 외부결제 모의 호출
        return ResponseEntity.ok("OK: orderId=" + orderId);
    }

    @PostMapping("/order-async")
    public ResponseEntity<?> orderAsync(@RequestBody @Valid OrderRequest req) {
        String traceId = UUID.randomUUID().toString();
        OrderMessage msg = OrderMessage.builder()
                .traceId(traceId)
                .userId(req.getUserId())
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .requestedAt(System.currentTimeMillis())
                .build();

        orderProducer.send(msg); // fire-and-forget

        return ResponseEntity.accepted().body(Map.of(
                "status", "QUEUED",
                "traceId", traceId
        ));
    }
}
