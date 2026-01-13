package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Product;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

    void sendSimpleEmail(String toEmail,String body,String subject);

    void sendOrderConfirmationEmail(Order order, Customer customer);

    void sendOrderReceipt(String toEmail, String orderId, String customerName,
                          List<Product> productList, double total) throws MessagingException;
}