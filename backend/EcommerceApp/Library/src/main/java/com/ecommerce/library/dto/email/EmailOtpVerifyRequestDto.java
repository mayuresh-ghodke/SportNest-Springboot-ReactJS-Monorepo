package com.ecommerce.library.dto.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailOtpVerifyRequestDto {
    @NotEmpty
    private String newEmail;

    @NotEmpty
    private String otp;
}
