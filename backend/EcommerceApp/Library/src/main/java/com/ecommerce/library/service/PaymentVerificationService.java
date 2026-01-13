package com.ecommerce.library.service;

import com.ecommerce.library.dto.PaymentVerificationRequest;

public interface PaymentVerificationService {
    void verifyPayment(PaymentVerificationRequest paymentVerificationRequest);
}
