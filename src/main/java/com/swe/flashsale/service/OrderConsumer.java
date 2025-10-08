package com.swe.flashsale.service;

import com.swe.flashsale.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    // 기존의 주문 처리 로직이 담긴 Service를 받기
    private final OrderService orderService;


    @KafkaListener(topics = "order-topic",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrder(OrderRequest request) {


        System.out.println("Kafka 주문 요청 수신 확인 및 처리 완료 : " + request);
    }
}