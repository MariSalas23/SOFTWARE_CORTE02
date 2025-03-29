package com.pragma.order_service.external.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private long transactionId;
    private Instant paymentDate;
    private long amount;
    private String paymentStatus;
    private String paymentMode;

}
