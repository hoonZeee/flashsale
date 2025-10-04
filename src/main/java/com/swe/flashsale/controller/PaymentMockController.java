package com.swe.flashsale.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentMockController {
    @Value("${mock.payment.delay-ms:200}")
    private int delayMs;

    @PostMapping("/_mock/payment")
    public ResponseEntity<Void> pay() {
        try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
        return ResponseEntity.ok().build();
    }
}