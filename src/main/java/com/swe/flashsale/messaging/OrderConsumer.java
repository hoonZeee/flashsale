package com.swe.flashsale.messaging;

import com.swe.flashsale.dto.OrderMessage;
import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.group-id}")
    public void onMessage(OrderMessage msg) {
        log.debug("[Kafka] received: traceId={}, user={}, product={}, qty={}",
                msg.getTraceId(), msg.getUserId(), msg.getProductId(), msg.getQuantity());


        OrderRequest req = new OrderRequest(msg.getUserId(), msg.getProductId(), msg.getQuantity());
        Long orderId = orderService.placeOrderCore(req);
        orderService.callPaymentAndLog(orderId);

        log.debug("[Kafka] processed orderId={} (traceId={})", orderId, msg.getTraceId());
    }
}
