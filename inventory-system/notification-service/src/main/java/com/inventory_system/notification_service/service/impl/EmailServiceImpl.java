package com.inventory_system.notification_service.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.inventory_system.notification_service.dto.event.ProductEvent;
import com.inventory_system.notification_service.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

    @Value("${notification.email.sender}")
    private String senderEmail;
    
    @Value("${notification.email.recipient}")
    private String recipientEmail;
    
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
	@Override
	public void sendLowStockEmail(ProductEvent product) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Low Stock Alert: " + product.getProductName());
        message.setText("The stock for product '" + product.getProductName() + "' is running low.\n" +
                        "Current stock: " + product.getStock() + "\n" +
                        "Threshold: " + product.getMinStockThreshold() + "\n" +
                        "Please restock soon!");
        message.setFrom(senderEmail); 

        mailSender.send(message);
	}

}