package com.pragma.payment_service.exception;

import com.pragma.payment_service.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderCustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> orderNotFoundException(OrderCustomException orderCustomException){
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(orderCustomException.getCode())
                .errorMessage(orderCustomException.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }
}
