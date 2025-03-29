package com.pragma.order_service.service;

import com.pragma.order_service.model.OrderRequest;
import com.pragma.order_service.model.OrderResponse;

public interface IOrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
