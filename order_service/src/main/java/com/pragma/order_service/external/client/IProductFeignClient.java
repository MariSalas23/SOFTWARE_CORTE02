package com.pragma.order_service.external.client;

import com.pragma.order_service.error.CustomOrderException;
import com.pragma.order_service.external.response.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "product-service")
public interface IProductFeignClient {

    @PutMapping("/product/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity);

    @GetMapping("/product/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable long id);

    default void fallback(Exception e){
        throw new CustomOrderException("UNAVAILABLE","Product Service is not available");
    }

}
