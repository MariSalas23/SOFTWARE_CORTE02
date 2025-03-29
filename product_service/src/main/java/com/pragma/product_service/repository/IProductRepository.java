package com.pragma.product_service.repository;

import com.pragma.product_service.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity,Long> {
    ProductEntity getProductEntityByProductId(long id);
}
