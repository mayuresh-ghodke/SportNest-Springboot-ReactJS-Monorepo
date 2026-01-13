package com.ecommerce.library.model;

import com.ecommerce.library.enumstatus.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //order_id is the primary key of the customers purchase order
    @Column(name = "order_id")
    private Long id;

    private Date orderDate;
    private Date deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private double totalPrice;
    private double tax;
    private int quantity;

    private boolean isAccept;
    private boolean isDelivered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;
}
