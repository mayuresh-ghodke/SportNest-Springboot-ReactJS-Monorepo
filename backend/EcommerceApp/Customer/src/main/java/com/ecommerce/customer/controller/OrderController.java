package com.ecommerce.customer.controller;

import com.ecommerce.customer.exception.ResourceNotFoundException;
import com.ecommerce.library.customerDto.*;
import com.ecommerce.library.dto.OrderDto;
import com.ecommerce.library.exception.CartEmptyException;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final PaymentOrderService paymentOrderService;
    private final AddressService addressService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> createShoppingOrder(Principal principal, HttpSession session) {
        if (principal == null) {
            log.warn("Unauthenticated attempt to create order");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Create shopping order request by user: {}", principal.getName());
        Customer customer = customerService.findByUsername(principal.getName());
        Address address = addressService.getAddressByCustomerId(customer.getId());
        if(address == null){
            log.warn("Order creation failed: address not found for user {}", customer.getUsername());
            throw new ResourceNotFoundException("Address is empty. Shopping order not created.");
        }

        ShoppingCart cart = customer.getCart();

        if (cart.getCartItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty.");
        }

        Order order = orderService.placeOrder(cart);
        if(order == null){
            log.warn("Shopping order not created for user {}", customer.getUsername());
            throw new RuntimeException("Your shopping order not created. Something went wrong.");
        }
        log.info("Order created successfully for user {}", customer.getUsername());
        session.removeAttribute("totalItems");

        OrderResponseDto orderResponseDto = ResponseDtoMapper.mapOrderDtoToOrderResponseDto(Mapper.mapOrderToOrderDto(order));
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponseDto>> getOrdersResponseDtos(Principal principal) {

        if (principal == null) {
            log.warn("Unauthenticated attempt to get all orders");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Customer customer = customerService.findByUsername(principal.getName());
        List<Order> orders = customer.getOrders();

        List<PaymentOrder> paymentOrders =
                paymentOrderService.findByCustomerIdOrderByPaymentCreatedAtDesc(customer.getId());

        Map<Long, PaymentOrderResponseDto> paymentMap =
                paymentOrders.stream()
                        .collect(Collectors.toMap(
                                po -> po.getOrder().getId(),
                                ResponseDtoMapper::mapPaymentOrderToPaymentOrderResponseDto,
                                (oldVal, newVal) ->
                                        newVal.getPaymentCreatedAt().isAfter(oldVal.getPaymentCreatedAt())
                                                ? newVal
                                                : oldVal
                        ));

        List<OrderResponseDto> response =
                orders.stream()
                        .map(order -> {
                            OrderDto orderDto = Mapper.mapOrderToOrderDto(order);
                            PaymentOrderResponseDto paymentDto =
                                    paymentMap.get(order.getId());

                            return ResponseDtoMapper.mapOrderDtoToOrderResponseDto(
                                    orderDto, paymentDto
                            );
                        })
                        .toList();

        return ResponseEntity.ok(response);
    }


    @PutMapping(value = "/cancel/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long orderId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Order order = orderService.getOrderByOrderId(orderId);
        if (!order.getCustomer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Order cancelledOrder = orderService.cancelOrder(orderId);
        if (cancelledOrder != null) {
            return ResponseEntity.ok("Order has been cancelled.");
        }
        return ResponseEntity.badRequest().body("Cancel failed");
    }

    @GetMapping(value = "/{id}/track", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTrackingResponseDto> trackOrder(
            @PathVariable("id") Long orderId, Principal principal) {

        if(principal == null){
            log.warn("Unauthenticated attempt to create order");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID-"+orderId);
        }

        // if customer logged in, but enter another users order id, then not allowed
        if(!order.getCustomer().getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        OrderTrackingResponseDto orderTrackResponseDto = new OrderTrackingResponseDto();
        orderTrackResponseDto.setOrderResponseDto(ResponseDtoMapper.mapOrderDtoToOrderResponseDto(
                Mapper.mapOrderToOrderDto(order)));
        return new ResponseEntity<>(orderTrackResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}/info")
    public ResponseEntity<OrderInformationResponseDto> getOrderInformation(@PathVariable("id") Long orderId,
                                                                           Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Order order = orderService.getOrderByOrderId(orderId);
        if (!order.getCustomer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Customer customer = order.getCustomer();
        Address address = addressService.getAddressByCustomerId(customer.getId());

        OrderResponseDto orderResponseDto = ResponseDtoMapper.mapOrderDtoToOrderResponseDto(Mapper.mapOrderToOrderDto(order));
        CustomerResponseDto customerResponseDto = Mapper.mapCustomerToCustomerResponseDto(customer);
        AddressResponseDto addressResponseDto = ResponseDtoMapper.mapAddressToAddressResponseDto(address);

        OrderInformationResponseDto orderInformationResponseDto = new OrderInformationResponseDto();
        orderInformationResponseDto.setAddressResponseDto(addressResponseDto);
        orderInformationResponseDto.setCustomerResponseDto(customerResponseDto);
        orderInformationResponseDto.setOrderResponseDto(orderResponseDto);

        return new ResponseEntity<>(orderInformationResponseDto, HttpStatus.OK);
    }
}


