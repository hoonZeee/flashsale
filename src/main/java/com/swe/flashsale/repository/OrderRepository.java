package com.swe.flashsale.repository;

import com.swe.flashsale.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> { }