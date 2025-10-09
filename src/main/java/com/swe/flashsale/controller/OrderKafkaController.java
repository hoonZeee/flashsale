package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaController {
    
    private final OrderProducerService orderProducerService;
    
    @PostMapping("/order-kafka")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody @Valid OrderRequest req) {
        try {
            // Kafka로 주문 요청 전송 (비동기)
            String requestId = orderProducerService.sendOrderRequest(
                    req.getUserId(), 
                    req.getProductId(), 
                    req.getQuantity()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ACCEPTED");
            response.put("requestId", requestId);
            response.put("message", "주문 요청이 접수되었습니다. 처리 중입니다.");
            
            log.info("Order request accepted: requestId={}, userId={}, productId={}, quantity={}", 
                    requestId, req.getUserId(), req.getProductId(), req.getQuantity());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to accept order request: userId={}, productId={}", 
                    req.getUserId(), req.getProductId(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "주문 요청 처리 중 오류가 발생했습니다.");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
