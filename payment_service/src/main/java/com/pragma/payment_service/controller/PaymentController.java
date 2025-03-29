package com.pragma.payment_service.controller;

import com.pragma.payment_service.model.payment.PaymentRequest;
import com.pragma.payment_service.model.payment.PaymentResponse;
import com.pragma.payment_service.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest){
        return new ResponseEntity<>(paymentService.doPayment(paymentRequest),HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable long orderId){
        PaymentResponse paymentResponse = paymentService.getPaymentByOrderId(orderId);
        return new ResponseEntity<>(paymentResponse,HttpStatus.OK);
    }
}
