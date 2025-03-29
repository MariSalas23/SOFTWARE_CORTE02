package com.pragma.payment_service.service;

import com.pragma.payment_service.entity.TransactionEntity;
import com.pragma.payment_service.exception.OrderCustomException;
import com.pragma.payment_service.model.payment.PaymentRequest;
import com.pragma.payment_service.model.payment.PaymentResponse;
import com.pragma.payment_service.repository.ITransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements IPaymentService{

    @Autowired
    ITransactionDetailsRepository transactionDetailsRepository;

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {

        log.info("Creating Payment with data: {}",paymentRequest);

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        transactionDetailsRepository.save(transactionEntity);

        log.info("Transaction completed with Id: {}", transactionEntity.getTransactionId());

        return transactionEntity.getTransactionId();

    }

    @Override
    public PaymentResponse getPaymentByOrderId(long orderId) {

        log.info("Searching payment with order id: {}", orderId);

        TransactionEntity transactionEntity = transactionDetailsRepository.findTransactionEntityByOrderId(orderId)
                .orElseThrow(() ->new OrderCustomException(HttpStatus.NOT_FOUND.toString(),"No payment found for order id: " +orderId));

        return PaymentResponse.builder()
                .transactionId(transactionEntity.getTransactionId())
                .paymentDate(transactionEntity.getPaymentDate())
                .amount(transactionEntity.getAmount())
                .paymentStatus(transactionEntity.getPaymentStatus())
                .paymentMode(transactionEntity.getPaymentMode())
                .build();
    }
}
