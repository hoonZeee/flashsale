package com.swe.flashsale.service;

import com.swe.flashsale.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "orders", groupId = "flashsale-group")
    public void consume(String message) {
        System.out.println("📥 Kafka message received: " + message);

        try {
            String cleaned = message.replaceAll("[{}\\\"]", "");
            String[] parts = cleaned.split(",");

            Long userId = null;
            Long productId = null;
            int quantity = 0;

            for (String part : parts) {
                String[] kv = part.trim().split(":");
                if (kv[0].trim().equals("userId")) {
                    userId = Long.parseLong(kv[1].trim());
                } else if (kv[0].trim().equals("productId")) {
                    productId = Long.parseLong(kv[1].trim());
                } else if (kv[0].trim().equals("quantity")) {
                    quantity = Integer.parseInt(kv[1].trim());
                }
            }

            if (userId != null && productId != null) {
                orderService.placeOrderCore(new OrderRequest(userId, productId, quantity));
                System.out.println("✅ Order processed via Kafka: userId=" + userId + ", productId=" + productId + ", quantity=" + quantity);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to parse Kafka message: " + message);
        }
    }
}
