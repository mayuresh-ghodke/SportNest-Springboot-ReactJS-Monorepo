package com.ecommerce.library.service;

import com.ecommerce.library.enumstatus.OtpPurpose;

public interface OtpService {
    void generateAndSendOtp(String email, String subject, OtpPurpose purpose);
    boolean verifyOtp(String email, String otp, OtpPurpose purpose);
    boolean isOtpVerified(String email, OtpPurpose purpose);
    void clearOtp(String email, OtpPurpose purpose);
}
