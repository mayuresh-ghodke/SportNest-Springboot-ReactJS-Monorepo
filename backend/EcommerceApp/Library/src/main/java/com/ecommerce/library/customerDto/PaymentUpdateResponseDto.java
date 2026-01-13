package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateResponseDto {
    private PaymentOrderResponseDto paymentOrder;
    private OrderResponseDto order;
}
