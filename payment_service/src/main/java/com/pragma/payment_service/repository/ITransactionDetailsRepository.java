package com.pragma.payment_service.repository;

import com.pragma.payment_service.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITransactionDetailsRepository extends JpaRepository<TransactionEntity,Long> {

     Optional<TransactionEntity> findTransactionEntityByOrderId(long orderId);
}
