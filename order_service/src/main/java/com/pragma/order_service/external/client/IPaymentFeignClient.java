package com.pragma.order_service.external.client;

import com.pragma.order_service.error.CustomOrderException;
import com.pragma.order_service.external.request.PaymentRequest;
import com.pragma.order_service.external.response.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "payment-service")
public interface IPaymentFeignClient {

    @PostMapping("/payment")
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    @GetMapping("/payment/{orderId}")
    ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable long orderId);

    default void fallback(Exception e){
        throw new CustomOrderException("UNAVAILABLE","Payment Service is not available");
    }

}
