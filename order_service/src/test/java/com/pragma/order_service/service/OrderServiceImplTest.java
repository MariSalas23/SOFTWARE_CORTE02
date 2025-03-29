package com.pragma.order_service.service;

import com.pragma.order_service.entity.OrderEntity;
import com.pragma.order_service.error.CustomOrderException;
import com.pragma.order_service.external.client.IPaymentFeignClient;
import com.pragma.order_service.external.client.IProductFeignClient;
import com.pragma.order_service.external.request.PaymentRequest;
import com.pragma.order_service.external.response.PaymentResponse;
import com.pragma.order_service.external.response.ProductResponse;
import com.pragma.order_service.model.OrderRequest;
import com.pragma.order_service.model.OrderResponse;
import com.pragma.order_service.model.PaymentMode;
import com.pragma.order_service.repository.IOrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IProductFeignClient productFeignClient;

    @Mock
    private IPaymentFeignClient paymentFeignClient;
    @InjectMocks
    IOrderService orderService = new OrderServiceImpl();

    @Test
    @DisplayName("Get Order - Success Scenario")
    void testWhenOrderSuccess(){

        OrderEntity orderEntity = getMockOrderEntity();

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(orderEntity));

        when(productFeignClient.getProductById(anyLong()))
                .thenReturn(getMockProductResponse());

        when(paymentFeignClient.getPaymentByOrderId(anyLong()))
                .thenReturn(getMockPaymentResponse());

        //Actual
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        //Verification
        verify(orderRepository, times(1)).findById(anyLong());
        verify(productFeignClient, times(1)).getProductById(anyLong());
        verify(paymentFeignClient, times(1)).getPaymentByOrderId(anyLong());

        //Assert
        assertNotNull(orderResponse);
        assertEquals(orderEntity.getOrderId(), orderResponse.getOrderId());
    }

    @Test
    @DisplayName("Get Order - No Order Found")
    void testWhenGetOrderNotFound(){

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,() -> orderService.getOrderDetails(1));
        assertEquals("ORDER_NOT_FOUND", exception.getCode());

        //Verification
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Place Order - Success Scenario")
    void testWhenPlaceOrderSuccess(){

        OrderEntity order = getMockOrderEntity();
        OrderRequest orderRequest = getMockOrderRequest();

        when(productFeignClient.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        when(paymentFeignClient.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenReturn(order);

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productFeignClient, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(paymentFeignClient, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);

    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .amount(1000)
                .build();
    }

    private ResponseEntity<PaymentResponse> getMockPaymentResponse() {
        return new ResponseEntity<>(PaymentResponse.builder()
                .transactionId(1)
                .paymentMode("CASH")
                .paymentDate(Instant.now())
                .paymentStatus("ACCEPTED")
                .amount(1000)
                .build(), HttpStatus.OK);
    }

    private ResponseEntity<ProductResponse> getMockProductResponse() {
        return new ResponseEntity<>(ProductResponse.builder()
                .productId(1)
                .productName("Iphone 15")
                .price(1000)
                .quantity(10)
                .build(), HttpStatus.OK);
    }

    private OrderEntity getMockOrderEntity() {
        return OrderEntity.builder()
                .orderId(1)
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .productId(1)
                .amount(1000)
                .quantity(1)
                .build();
    }

}