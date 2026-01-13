package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private Date orderDate;
    private Date deliveryDate;

    private String orderStatus;
    private boolean accepted;
    private boolean delivered;

    private double totalPrice;
    private int quantity;

    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;

    private String deliveryPersonName;

    // order items
    private List<OrderDetailResponseDto> items;
}

