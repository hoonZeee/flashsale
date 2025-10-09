package com.swe.flashsale.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMessage {
    private String traceId;     // 중복/추적용
    private Long userId;
    private Long productId;
    private Integer quantity;
    private long requestedAt;
}
