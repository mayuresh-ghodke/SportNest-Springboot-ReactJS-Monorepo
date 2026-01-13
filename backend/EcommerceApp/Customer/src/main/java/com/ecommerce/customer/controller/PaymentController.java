package com.ecommerce.customer.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ecommerce.customer.exception.ResourceNotFoundException;
import com.ecommerce.library.customerDto.CreatePaymentOrderResponse;
import com.ecommerce.library.customerDto.OrderResponseDto;
import com.ecommerce.library.enumstatus.PaymentStatus;
import com.ecommerce.library.exception.UnauthorizedException;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.dto.CreatePaymentOrderRequest;
import com.ecommerce.library.dto.PaymentVerificationRequest;
import com.ecommerce.library.model.PaymentOrder;
import com.ecommerce.library.service.*;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.*;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment") // prefix for API endpoints
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class PaymentController {

    /*
    * The class we are looking at is the Core Entry Point of the Razorpay Java SDK.
    * It acts as a "Gateway" or a "Wrapper" that allows your Spring Boot application to communicate
    * with Razorpay's servers without you having to write complex HTTP requests manually.

    When you initialize new RazorpayClient(key, secret), it creates specialized
    * "Clients" (like payments, orders, refunds) using your credentials for authentication.
    *
    * OrderClient is a specific sub-service designed to handle the Orders API.
    * In the world of Razorpay, an "Order" is a server-side entity that you must create before the
    * payment starts. It ties a specific amount and currency to a unique order_id.

    Why we are using orders?
    In our code, we use razorpayClient.orders.create(options).
    * Here is what happens under the hood:
        Preparation: You provide a JSONObject containing the amount (in paise), currency, and receipt.
        Request: The orders client sends a POST request to https://api.razorpay.com/v1/orders.
        Response: Razorpay returns an Order object.
        *
        Purpose: This tells Razorpay: "I am expecting a payment of ₹X.
        * Please give me an ID so I can track it."
    * */
    private final RazorpayClient razorpayClient;
    private final CustomerService customerService;
    private final PaymentOrderService paymentOrderService;
    private final OrderService orderService;
    private final PaymentVerificationService paymentVerificationService;
    private final EmailSenderService emailSenderService;

    /*
    *   Whenever customer places an order, request comes to OrderController,
    *   Order get saved into db, and response sent back to client side,
    *
    *   Client send req with placed shopping orders orderId to PaymentController to /create-order endpoint
    *   it creates and PaymentOrder uses shopping orders orderId as Fk, and creating new PaymentOrder.
    *   and sent response back to check out page, with orderId (Razorpay orderId, amount, receipt, currency)
    *
    *   Customer makes payment using Razorpay pop-up using above response data, and req to /update-order
    *   to save the payment order for that particular Shopping orderId,
    *   and make changes as PaymentStatus to 'PAID' and as response send whole OrderResponseDto
    *   to client.
    * */

    // CREATE RAZORPAY ORDER
    @PostMapping(value = "/create-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePaymentOrderResponse> createPaymentOrder(@RequestBody CreatePaymentOrderRequest request,
            Principal principal){

        log.info("Create payment order request received");

        try {
            if (principal == null) {
                log.warn("Unauthorized payment order creation attempt");
                throw new UnauthorizedException("Login required.");
            }

            String username = principal.getName();
            Long customerId = customerService.getCustomerId(username);

            String currency = request.getCurrency() != null ? request.getCurrency() : "INR";

            LocalDateTime now = LocalDateTime.now();
            String receipt = "txn_" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + customerId;

            // creating JSONObject object to pass to razorpayClient.orders.create() take JSONObject as param;
            JSONObject options = new JSONObject();
            options.put("amount", (int)(request.getAmount() * 100)); // in paise
            options.put("currency", currency);
            options.put("receipt", receipt);

            log.info("Creating Razorpay order for customerId={}, amount={}, currency={}",
                    customerId, request.getAmount(), currency);
            Order order = razorpayClient.orders.create(options);

            System.out.println("Razorpay Order data : " + order.toString());

            log.info("Created Razorpay order for customerId={}, amount={}, currency={}",
                    customerId, request.getAmount(), currency);

            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setAmount(order.get("amount").toString());
            paymentOrder.setOrderId(order.get("id"));
            paymentOrder.setPaymentId(null);
            paymentOrder.setPaymentStatus(PaymentStatus.CREATED.toString());
            paymentOrder.setReceipt(receipt);
            paymentOrder.setPaymentCreatedAt(LocalDateTime.now());
            paymentOrder.setCustomer(customerService.findByUsername(username));

            com.ecommerce.library.model.Order placedOrder = orderService.getOrderByOrderId(request.getShoppingOrderId());
            if(placedOrder == null){
                log.error("Payment order creation failed: shoppingOrderId={} not found",
                        request.getShoppingOrderId());
                throw new ResourceNotFoundException("Shopping order not found.");
            }
            paymentOrder.setOrder(placedOrder);

            paymentOrderService.save(paymentOrder);
            log.info("PaymentOrder saved successfully for shoppingOrderId={}, paymentStatus={}",
                    placedOrder.getId(), PaymentStatus.CREATED);

            CreatePaymentOrderResponse response = new CreatePaymentOrderResponse(
                    order.get("id"),
                    request.getAmount(),
                    currency,
                    receipt
            );

            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            log.error("Razorpay order creation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE PAYMENT STATUS
    @PostMapping(value = "/update-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> updateOrder(Principal principal, @RequestBody
                                         PaymentVerificationRequest paymentVerificationRequest) {
        log.info("Payment verification request received");

        if (principal == null) {
            log.warn("Unauthorized payment verification attempt");
            throw new UnauthorizedException("Login required.");
        }
        log.debug("Fetching payment order for razorpayOrderId={}",paymentVerificationRequest.getRazorpayOrderId());

        // here findPaymentOrderByOrderId : here orderId means payment order id
        PaymentOrder paymentOrder = paymentOrderService.findPaymentOrderByOrderId(paymentVerificationRequest.getRazorpayOrderId());

        // first check, if PaymentOrder's PaymentStatus
        // if already PAID return immediately to reduce multiple api hit
        // otherwise proceed
        if (PaymentStatus.PAID.toString().equals(paymentOrder.getPaymentStatus())) {
            log.warn("Duplicate payment update for 'PAID' attempt ignored for razorpayOrderId={}",
                    paymentVerificationRequest.getRazorpayOrderId());
            return ResponseEntity.ok(
                    ResponseDtoMapper.mapOrderDtoToOrderResponseDto(
                            Mapper.mapOrderToOrderDto(paymentOrder.getOrder()),
                            ResponseDtoMapper.mapPaymentOrderToPaymentOrderResponseDto(paymentOrder)
                    )
            );
        }

        log.info("Verifying payment signature for razorpayOrderId={}",paymentVerificationRequest.getRazorpayOrderId());

        // verify the payment with signature, if yes, then proceed,
        // else, throws exception
        paymentVerificationService.verifyPayment(paymentVerificationRequest);

        log.info("Payment verified successfully, paymentId={}", paymentVerificationRequest.getRazorpayPaymentId());

        paymentOrder.setPaymentId(paymentVerificationRequest.getRazorpayPaymentId());

        paymentOrder.setPaymentStatus(PaymentStatus.PAID.toString());
        paymentOrder.setPaymentCompletedAt(LocalDateTime.now());

        PaymentOrder savedPaymentOrder = paymentOrderService.save(paymentOrder);

        // update the Order (Shopping order) entity as well by getting Order data using Payment_Order tables
        // order as FK
        com.ecommerce.library.model.Order order = savedPaymentOrder.getOrder();

        log.info("Sending order confirmation email for orderId={}", order.getId());

        // notify customers about order confirmation
        emailSenderService.sendOrderConfirmationEmail(order, order.getCustomer());

        // returning, OrderResponseDto with
        // Order details, and updated payment order details
        OrderResponseDto orderResponseDto =
               ResponseDtoMapper.mapOrderDtoToOrderResponseDto(Mapper.mapOrderToOrderDto(order),
                       ResponseDtoMapper.mapPaymentOrderToPaymentOrderResponseDto(savedPaymentOrder)
        );
        return ResponseEntity.ok(orderResponseDto);
    }
}


