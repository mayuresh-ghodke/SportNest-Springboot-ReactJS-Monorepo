package com.ecommerce.library.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.PaymentOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    
    PaymentOrder findByOrderId(String orderId);

    @EntityGraph(attributePaths = "order")
    List<PaymentOrder> findByCustomerId(Long customerId);

    @EntityGraph(attributePaths = "order")
    List<PaymentOrder> findByCustomerIdOrderByPaymentCreatedAtDesc(Long customerId);

    // find CREATED payment for shopping order
    Optional<PaymentOrder> findByOrder_IdAndPaymentStatus(
            Long shoppingOrderId,
            String paymentStatus
    );

    // find latest payment (safety)
    Optional<PaymentOrder> findTopByOrder_IdOrderByPaymentCreatedAtDesc(
            Long shoppingOrderId
    );
}
