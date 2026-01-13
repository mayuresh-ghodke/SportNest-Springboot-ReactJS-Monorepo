package com.ecommerce.library.service.impl;

import java.util.List;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.EmailSenderService;
import com.ecommerce.library.utils.PdfGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleEmail(String toEmail,String body,String subject){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setFrom("ghodkemayuresh86@gmail.com");
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendOrderConfirmationEmail(Order order, Customer customer) {

        try {
            String toEmail = customer.getUsername();
            String subject = "Payment received. Your order has been placed successfully – SportNest";
            String firstName = customer.getFirstName();
            String lastName = customer.getLastName();

            List<Product> productsList = orderDetailRepository.getProductsByOrderId(order.getId());
            StringBuilder productDetails = new StringBuilder();

            double total = 0.0;
            for (Product product : productsList) {
                Long productId = product.getId();
                int quantity = orderDetailRepository.getQuantityByProductIdAndOrderId(productId, order.getId());
                double unitPrice = product.getCostPrice();
                double totalPrice = quantity * unitPrice;
                total += totalPrice;

                productDetails.append("Product: ").append(product.getName())
                        .append(" | Qty: ").append(quantity)
                        .append(" | Unit Price: ₹").append(unitPrice)
                        .append(" | Total: ₹").append(totalPrice)
                        .append("\n");
            }

            // 📄 Generate PDF receipt
            byte[] pdfBytes = PdfGenerator.generateOrderReceiptPdf(
                    String.valueOf(order.getId()),
                    firstName + " " + lastName,
                    productsList,
                    total
            );

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);

            String body = "Dear " + firstName + " " + lastName + ",\n\n"
                    + "Thank you for shopping with Sport Shop!\n\n"
                    + "Order ID: " + order.getId() + "\n"
                    + "Total Items: " + order.getQuantity() + "\n"
                    + "Total Amount: ₹" + order.getTotalPrice() + "\n\n"
                    + "Items:\n" + productDetails.toString()
                    + "\n\nYou can track order with given OrderId. Your receipt is attached as a PDF.\n\n"
                    + "Regards,\nSport Shop Team";

            helper.setText(body, false);
            helper.addAttachment(order.getId()+"_order-receipt.pdf", new ByteArrayResource(pdfBytes));

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error sending order confirmation email for Order ID: " + order.getId() + " - " + e.getMessage());
            //e.printStackTrace();
        }
    }

    @Override
    public void sendOrderReceipt(String toEmail, String orderId, String customerName,
                                 List<Product> productList, double total) throws MessagingException {

        // Generate the PDF receipt
        byte[] pdfBytes = PdfGenerator.generateOrderReceiptPdf(orderId, customerName, productList, total);

        // Create email message
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your SPORTNEST Order Receipt");
        helper.setText("Dear " + customerName + ",\n\nPlease find your order receipt attached.\n\nThank you for shopping with SPORTNEST!", false);

        // Attach the PDF
        helper.addAttachment(orderId+"receipt.pdf", new ByteArrayResource(pdfBytes));

        // Send the email
        javaMailSender.send(message);
    }

}
