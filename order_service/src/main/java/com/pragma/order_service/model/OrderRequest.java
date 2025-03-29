package com.pragma.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private long productId;
    private long quantity;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private PaymentMode paymentMode;

}
