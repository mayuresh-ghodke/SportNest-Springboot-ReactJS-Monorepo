package com.ecommerce.library.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.ecommerce.library.enumstatus.OtpPurpose;
import com.ecommerce.library.dto.OtpData;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ecommerce.library.service.OtpService;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final JavaMailSender javaMailSender;

    @Override
    public void generateAndSendOtp(String email, String subject, OtpPurpose purpose) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        otpStore.put(email + ":" + purpose, new OtpData(otp, expiryTime, false, purpose));

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }

    @Override
    public boolean verifyOtp(String email, String otp, OtpPurpose purpose) {
        String key = email + ":" + purpose;
        OtpData data = otpStore.get(key);

        if (data == null || data.getExpiryTime().isBefore(LocalDateTime.now())) return false;

        if (data.getOtp().equals(otp)) {
            data.setVerified(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOtpVerified(String email, OtpPurpose purpose) {
        OtpData data = otpStore.get(email + ":" + purpose);
        return data != null && data.isVerified();
    }

    @Override
    public void clearOtp(String email, OtpPurpose purpose) {
        otpStore.remove(email + ":" + purpose);
    }
}

