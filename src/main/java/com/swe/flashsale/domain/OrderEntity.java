package com.swe.flashsale.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Table(name = "orders")
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    public static OrderEntity create(Long userId, Long productId, int quantity) {
        return OrderEntity.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public enum Status { CREATED, PAID, FAILED }
}