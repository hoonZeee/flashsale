package com.swe.flashsale.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** 남은 재고 */
    private int stock;

    /** 낙관적 락(경합 시 Version 오류 유발) */
    @Version
    private Long version;

    public void reduceStock(int qty) {
        if (stock < qty) throw new IllegalStateException("재고 부족");
        this.stock -= qty;
    }
}