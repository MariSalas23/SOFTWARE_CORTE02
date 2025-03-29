package com.pragma.cloud_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallback(){
        return "Order Service is Down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallback(){
        return "Product Service is Down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallback(){
        return "Payment Service is Down";
    }
}
