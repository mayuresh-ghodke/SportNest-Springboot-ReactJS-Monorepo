package com.ecommerce.library.dto;

import com.ecommerce.library.enumstatus.OtpPurpose;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OtpData {
    private String otp;
    private LocalDateTime expiryTime;
    private boolean verified;
    private OtpPurpose purpose;
}


