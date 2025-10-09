package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderMessage;
import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderKafkaController {

    private final OrderProducerService orderProducerService;

    @PostMapping("/order-kafka")
    public ResponseEntity<String> placeOrderKafka(@RequestBody OrderRequest req) {
        String requestId = UUID.randomUUID().toString();
        OrderMessage message = OrderMessage.builder()
                .requestId(requestId)
                .userId(req.getUserId())
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .build();
        orderProducerService.sendOrderRequest(message);
        return ResponseEntity.ok("{\"requestId\":\"" + requestId + "\",\"message\":\"주문 요청이 접수되었습니다. 처리 중입니다.\",\"status\":\"ACCEPTED\"}");
    }
}
