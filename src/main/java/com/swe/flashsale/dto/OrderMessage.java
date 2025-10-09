package com.swe.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMessage {
    private String requestId;
    private Long userId;
    private Long productId;
    private Integer quantity;
}
