package com.pragma.order_service.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.order_service.error.CustomOrderException;
import com.pragma.order_service.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try {

            ErrorResponse errorResponse = objectMapper.readValue(
                    response.body().asInputStream(),
                    ErrorResponse.class);

            return new CustomOrderException(
                    errorResponse.getErrorCode(),
                    errorResponse.getErrorMessage());

        } catch (IOException e) {

            throw new CustomOrderException("INTERNAL_SERVER_ERROR", "ERROR ON MAPPING FEIGN ERROR");
        }
    }
}
