package com.swe.flashsale.config;

import com.swe.flashsale.domain.Product;
import com.swe.flashsale.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            productRepository.deleteAll();
            Product product = Product.builder()
                    .name("한정판 스니커즈")
                    .stock(1000)
                    .version(0L)
                    .build();
            productRepository.save(product);
            System.out.println("초기 데이터 세팅 완료 (재고 1000개)");
        };
    }
}