package com.swe.flashsale.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}