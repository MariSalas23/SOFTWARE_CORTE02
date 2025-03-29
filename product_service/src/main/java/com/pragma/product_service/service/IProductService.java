package com.pragma.product_service.service;

import com.pragma.product_service.model.ProductRequest;
import com.pragma.product_service.model.ProductResponse;

public interface IProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);

    void reduceQuantity(long id, long quantity);
}
