package com.swe.flashsale.controller;

import com.swe.flashsale.dto.OrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderKafkaController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/order-kafka")
    public ResponseEntity<String> orderViaKafka(@RequestBody @Valid OrderRequest req) {
        kafkaTemplate.send("orders-topic", req);
        return ResponseEntity.ok("주문 메시지 전송 완료");
    }
}
