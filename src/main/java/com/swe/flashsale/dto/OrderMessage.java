package com.swe.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {
    private String requestId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime requestTime;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
}
