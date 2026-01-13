package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResponseDto {

    private Long paymentOrderId;
    private String amount;
    private String receipt;
    private String status;
    private String firstName;
    private String lastName;
    private String paymentId;
    private LocalDateTime paymentCreatedAt;
    private LocalDateTime paymentCompletedAt;
}
