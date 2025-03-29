package com.pragma.order_service.error;

import lombok.Data;

@Data
public class CustomOrderException extends RuntimeException{

    private final String code;
    public CustomOrderException(String code, String message){
        super(message);
        this.code = code;
    }
}
