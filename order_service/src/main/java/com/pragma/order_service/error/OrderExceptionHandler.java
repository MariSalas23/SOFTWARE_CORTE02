package com.pragma.order_service.error;

import com.pragma.order_service.external.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(CustomOrderException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> customOrderException(CustomOrderException customOrderException){
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(customOrderException.getMessage())
                .errorCode(customOrderException.getCode()).build(), HttpStatus.NOT_FOUND);
    }


}
