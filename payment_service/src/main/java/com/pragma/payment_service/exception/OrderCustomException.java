package com.pragma.payment_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrderCustomException extends RuntimeException{

    private final String code;
    public OrderCustomException(String code, String message){
        super(message);
        this.code = code;
    }
}
