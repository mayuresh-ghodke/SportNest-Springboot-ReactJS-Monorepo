package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_order")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentOrderId;

    //orderId is the order id of the razorpay payment, that is received from razorpay
    private String orderId;

    private String amount;

    private String receipt;

    private String paymentStatus;

    private String paymentId;

    private LocalDateTime paymentCreatedAt;

    private LocalDateTime paymentCompletedAt;

    @ManyToOne
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_order_id")
    private Order order;
}
