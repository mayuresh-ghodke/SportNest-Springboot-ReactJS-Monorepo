package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderDto {

    private String razorpayPaymentOrderId;
    private String amount;
    private String receipt;
    private String status;
    private String paymentId;
    private LocalDateTime paymentCreatedAt;
    private LocalDateTime paymentCompletedAt;

}
