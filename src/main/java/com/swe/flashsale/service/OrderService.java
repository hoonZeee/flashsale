package com.swe.flashsale.service;

import com.swe.flashsale.domain.*;
import com.swe.flashsale.dto.OrderRequest;
import com.swe.flashsale.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderEventRepository eventRepository;
    private final RestClient restClient;

    @Value("${server.port:8080}")
    private int serverPort;

    /**
     * 주문→재고차감→결제호출→로그 기록, 모두 동기 순차 처리.
     * 트랜잭션은 재고차감+주문저장까지만 묶고, 결제는 트랜잭션 밖에서 네트워크 지연 유발.
     */
    @Transactional
    public Long placeOrderCore(OrderRequest req) {
        return placeOrderCore(req.getUserId(), req.getProductId(), req.getQuantity());
    }
    
    /**
     * Kafka Consumer에서 호출하는 메서드
     */
    @Transactional
    public Long placeOrderCore(Long userId, Long productId, Integer quantity) {
        // 1) 재고 행 잠금 후 차감
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        product.reduceStock(quantity);

        // 2) 주문 생성 저장
        OrderEntity order = OrderEntity.create(userId, productId, quantity);
        order = orderRepository.save(order);

        // 3) 이벤트(로그) 기록 - 주문 생성
        eventRepository.save(OrderEvent.of(order.getId(), "ORDER_CREATED", "주문 생성"));
        eventRepository.save(OrderEvent.of(order.getId(), "STOCK_REDUCED", "재고 차감"));

        return order.getId();
    }

    /** 결제 호출(모의) + 결과 이벤트 기록 */
    public void callPaymentAndLog(Long orderId) {
        // 로컬 모의 결제 엔드포인트 호출로 네트워크 I/O 부하 유발
        String url = "http://localhost:" + serverPort + "/_mock/payment";
        try {
            restClient.post().uri(url).retrieve().toBodilessEntity();
            eventRepository.save(OrderEvent.of(orderId, "PAYMENT_OK", "결제 성공"));
        } catch (Exception e) {
            eventRepository.save(OrderEvent.of(orderId, "PAYMENT_FAIL", e.getMessage()));
        }
    }
}