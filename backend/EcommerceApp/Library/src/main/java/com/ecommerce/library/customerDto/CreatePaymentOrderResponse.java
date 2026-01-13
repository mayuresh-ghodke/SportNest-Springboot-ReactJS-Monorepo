package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentOrderResponse {
    private String orderId;
    private double amount;
    private String currency;
    private String receipt;
}
