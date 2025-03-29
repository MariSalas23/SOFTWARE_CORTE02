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
import com.pragma.order_service.repository.IOrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements IOrderService{

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IProductFeignClient productFeignClient;

    @Autowired
    private IPaymentFeignClient paymentFeignClient;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order request: {}", orderRequest);

        productFeignClient.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("Creating order with status CREATED");

        OrderEntity orderEntity = OrderEntity.builder()
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .amount(orderRequest.getAmount())
                .quantity(orderRequest.getQuantity())
                .build();

        orderEntity = orderRepository.save(orderEntity);

        log.info("Calling payment service to complete payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderEntity.getOrderId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getAmount())
                .build();

        String orderStatus;

        try {
            paymentFeignClient.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing order status to placed");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.info("Error occurred on payment. Changing order status to payment failed");
            orderStatus = "PAYMENT_FAILED";
        }

        orderEntity.setOrderStatus(orderStatus);

        orderRepository.save(orderEntity);

        log.info("Order placed successfully with order id: {}", orderEntity.getOrderId());

        return orderEntity.getOrderId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Getting order details for order id: {}", orderId);

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomOrderException("ORDER_NOT_FOUND","Order not found with id: " + orderId));

        log.info("Fetching product details with id: {}",orderEntity.getProductId());

        ProductResponse productResponse = productFeignClient.getProductById(orderEntity.getProductId()).getBody();

        PaymentResponse paymentResponse = paymentFeignClient.getPaymentByOrderId(orderEntity.getOrderId()).getBody();

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .transactionId(paymentResponse.getTransactionId())
                .paymentDate(paymentResponse.getPaymentDate())
                .amount(paymentResponse.getAmount())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse.builder()
                .orderId(orderEntity.getOrderId())
                .orderStatus(orderEntity.getOrderStatus())
                .orderDate(orderEntity.getOrderDate())
                .amount(orderEntity.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
