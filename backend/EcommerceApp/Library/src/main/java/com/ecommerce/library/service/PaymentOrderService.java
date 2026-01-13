package com.ecommerce.library.service;

import com.ecommerce.library.model.PaymentOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentOrderService {

    PaymentOrder save(PaymentOrder paymentOrder);

    PaymentOrder findPaymentOrderByOrderId(String orderId);

    List<PaymentOrder> findByCustomerId(Long id);

    List<PaymentOrder> findByCustomerIdOrderByPaymentCreatedAtDesc(Long id);
}
