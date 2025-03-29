package com.pragma.product_service.service;

import com.pragma.product_service.entity.ProductEntity;
import com.pragma.product_service.error.ProductNotFoundException;
import com.pragma.product_service.error.ProductServiceCustomException;
import com.pragma.product_service.model.ProductRequest;
import com.pragma.product_service.model.ProductResponse;
import com.pragma.product_service.repository.IProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements IProductService{

    @Autowired
    private IProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");

        ProductEntity product
                = ProductEntity.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long id) {

        log.info("Getting product with Id: {}", id);

        ProductEntity productEntity = productRepository.getProductEntityByProductId(id);

        return Optional.ofNullable(productEntity)
                .map(entity -> {
                    ProductResponse productResponse = new ProductResponse();
                    copyProperties(productEntity,productResponse);
                    return productResponse;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @Override
    public void reduceQuantity(long id, long quantity) {
        log.info("Reducing quantity for product with Id: {}", id);

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductServiceCustomException("Product not found with ID: " + id, "PRODUCT_NOT_FOUND"));

        if(productEntity.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product does not have enought quantity","INSUFFICIENT_QUANTITY");
        }

        productEntity.setQuantity(productEntity.getQuantity()-quantity);
        productRepository.save(productEntity);

        log.info("Product quantity updated successfully");
    }
}
