package com.swe.flashsale.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class OrderEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String type;      // e.g. ORDER_CREATED, STOCK_REDUCED, PAYMENT_OK
    private String message;
    private LocalDateTime createdAt;

    public static OrderEvent of(Long orderId, String type, String message) {
        return OrderEvent.builder()
                .orderId(orderId)
                .type(type)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
    }
}