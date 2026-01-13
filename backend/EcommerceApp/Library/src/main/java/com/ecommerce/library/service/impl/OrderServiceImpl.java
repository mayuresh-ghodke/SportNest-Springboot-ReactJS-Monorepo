package com.ecommerce.library.service.impl;

import com.ecommerce.library.enumstatus.OrderStatus;
import com.ecommerce.library.exception.InsufficientStockException;
import com.ecommerce.library.exception.ResourceNotFoundException;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public Order placeOrder(ShoppingCart shoppingCart) {
        Customer customer = shoppingCart.getCustomer();
        log.info("Placing order for customerId={}", customer.getId());
        Address address = addressRepository.findAddressByCustomerId(customer.getId());
        if(address == null){
            log.error("Order failed: address missing for customerId={}", customer.getId());
            throw new ResourceNotFoundException("Address is empty.");
        }
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setTax(0);
        order.setQuantity(shoppingCart.getTotalItems());
        order.setAccept(false);

        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartItem item : shoppingCart.getCartItems()) {

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = item.getProduct();
            orderDetail.setProduct(product);

            log.debug("Reducing stock: productId={}, quantity={}",
                    product.getId(), item.getQuantity());

            int updatedRows = productService.reduceProductStock(product.getId(), item.getQuantity());

            if(updatedRows == 0){
                log.error("Insufficient stock: productId={}", product.getId());
                throw new InsufficientStockException("Stock is not available. Insufficient Stock.");
            }
            // here we set quantity of products in cart into order detail table
            orderDetail.setProductQuantity(item.getQuantity());

            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        log.info("Order placed successfully: orderId={}", savedOrder.getId());
        savedOrder.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());

        return savedOrder;
    }

//    @Override
//    @Transactional
//    public Order placeOrder(ShoppingCart shoppingCart) {
//        Order order = new Order();
//        order.setOrderDate(new Date());
//        order.setOrderStatus(OrderStatus.PENDING);
//        order.setTotalPrice(shoppingCart.getTotalPrice());
//        order.setTax(0);
//        order.setQuantity(shoppingCart.getTotalItems());
//        order.setAccept(false);
//
//        order.setCustomer(shoppingCart.getCustomer());
//
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//        for (CartItem item : shoppingCart.getCartItems()) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setProduct(item.getProduct());
//            Product product = item.getProduct();
//
//            int updatedRows = productService.reduceProductStock(product.getId(), item.getQuantity());
//
//            if(updatedRows == 0){
//                throw new InsufficientStockException("Stock is not available. Insufficient Stock.");
//            }
//            // here we set quantity of products in cart into order detail table
//            orderDetail.setProductQuantity(item.getQuantity());
//
//            detailRepository.save(orderDetail);
//            orderDetailList.add(orderDetail);
//        }
//
//        order.setOrderDetailList(orderDetailList);
//        cartService.deleteCartById(shoppingCart.getId());
//        return orderRepository.save(order);
//    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findAllOrders() {
        List<Order> ordersList = orderRepository.findAll();
        return ordersList;
    }

    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.findOrderById(id);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setAccept(true);

        // Calculate a random delivery date within the next 8 days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Random random = new Random();
        int daysToAdd = random.nextInt(8) + 1; // Add 1 to ensure minimum 1 day
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order cancelOrder(Long id) {
        log.info("Cancelling order: orderId={}", id);
        Order order = orderRepository.findOrderById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + id + " not found.");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order cancelled successfully: orderId={}", id);
        return order;
    }


    @Override
    public Order getOrderByOrderId(Long id) {
        Order order = orderRepository.findOrderById(id);
        if(order==null){
            throw new ResourceNotFoundException("Order with ID-"+id+" not found.");
        }
        return order;
    }

    @Override
    public Order assignDeliveryPerson(Long orderId, DeliveryPerson deliveryPerson) {
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
        order.setDeliveryPerson(deliveryPerson);
        order.setOrderStatus(OrderStatus.ASSIGNED);
        return orderRepository.save(order);
    }

    // to set delivery status to delivered
    @Override
    public OrderStatus updateDeliveryStatusOnDelivery(Long orderId, boolean isDelivered){
        isDelivered = true;
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
        order.setDelivered(isDelivered);
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setDeliveryDate(new Date());
        orderRepository.save(order);
        return OrderStatus.DELIVERED;
    }

    @Override
    public List<Order> getOrdersByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerRepository.findCustomerById(customerId);
        return customer.getOrders()
        .stream().filter(order->order.getOrderStatus().equals(orderStatus)).toList();
    }

    public List<Order> getAllPendingOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> pendingOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(order.getOrderStatus()==OrderStatus.PENDING){
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }
    public List<Order> getAllAcceptedOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> acceptedOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(order.getOrderStatus()==OrderStatus.CONFIRMED){
                acceptedOrders.add(order);
            }
        }
        return acceptedOrders;
    }

    public List<Order> getAllDeliveredOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> deliveredOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(order.getOrderStatus()==OrderStatus.DELIVERED){
                deliveredOrders.add(order);
            }
        }
        return deliveredOrders;
    }

    @Override
    public Order markOrderAsShipped(Long id) {
        Order order = orderRepository.findOrderById(id);
        order.setOrderStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }
}
