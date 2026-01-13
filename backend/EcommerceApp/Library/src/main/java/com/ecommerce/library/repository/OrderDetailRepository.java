package com.ecommerce.library.repository;

import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od.product FROM OrderDetail od WHERE od.order.id = ?1")
    List<Product> getProductsByOrderId(Long orderId);

    @Query("SELECT od.productQuantity FROM OrderDetail od WHERE od.product.id = :productId AND od.order.id = :orderId")
    int getQuantityByProductIdAndOrderId(Long productId, Long orderId);
}
