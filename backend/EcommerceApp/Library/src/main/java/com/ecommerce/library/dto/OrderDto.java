package com.ecommerce.library.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;
    private Date orderDate;
    private Date deliveryDate;

    private String orderStatus;   // PLACED, SHIPPED, DELIVERED
    private double totalPrice;
    private int totalQuantity;

    private boolean assigned;
    private boolean delivered;

    private List<OrderDetailDto> items;

    private DeliveryPersonDto deliveryPerson;
}

