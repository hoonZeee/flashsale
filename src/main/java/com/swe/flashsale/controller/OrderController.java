package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order-direct")
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequest req) {
        Long orderId = orderService.placeOrderCore(req);  // 재고락+주문 저장
        orderService.callPaymentAndLog(orderId);          // 외부결제 모의 호출
        return ResponseEntity.ok("OK: orderId=" + orderId);
    }
}