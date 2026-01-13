package com.ecommerce.library.service.impl;

import com.ecommerce.library.config.RazorpayConfig;
import com.ecommerce.library.dto.PaymentVerificationRequest;
import com.ecommerce.library.service.PaymentVerificationService;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentVerificationServiceImpl
        implements PaymentVerificationService {

    private final RazorpayConfig razorpayConfig;

    @Override
    public void verifyPayment(PaymentVerificationRequest request) {
        try {
            log.debug("Verifying Razorpay signature");
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());
            Utils.verifyPaymentSignature(attributes, razorpayConfig.getKeySecret());
            log.info("Payment signature verified successfully");
        }
        catch (Exception e) {
            log.info("Payment signature verification failed.");
            throw new RuntimeException("Payment verification exception.");
        }
    }
}

