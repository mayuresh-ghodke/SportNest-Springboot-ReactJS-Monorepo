package com.ecommerce.library.service;

import com.ecommerce.library.model.DeliveryPerson;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.enumstatus.OrderStatus;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;


public interface OrderService {
    Order placeOrder(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findAllOrders();

    Order acceptOrder(Long id);

    Order cancelOrder(Long id);

    Order assignDeliveryPerson(Long orderId, DeliveryPerson deliveryPerson);

    //Order updatePaymentIdandStatus(Long id);

    Order getOrderByOrderId(Long id);

    OrderStatus updateDeliveryStatusOnDelivery(Long orderId, boolean isDelivered);

    List<Order> getOrdersByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> getAllPendingOrders();

    List<Order> getAllDeliveredOrders();

    List<Order> getAllAcceptedOrders();

    Order markOrderAsShipped(Long id);

}
