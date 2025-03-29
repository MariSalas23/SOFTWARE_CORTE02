package com.pragma.payment_service.service;

import com.pragma.payment_service.model.payment.PaymentRequest;
import com.pragma.payment_service.model.payment.PaymentResponse;

public interface IPaymentService {
    Long doPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentByOrderId(long orderId);
}
