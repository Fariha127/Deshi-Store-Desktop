package com.example.finding_bd_products;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailService {
    private static EmailService instance;
    private static final String FROM_EMAIL = "deshistore.main@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "meta gmqg cjkb gzld"; // Replace with your app password
    
    private EmailService() {}
    
    public static EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }
    
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }
    
    public boolean sendVerificationEmail(String toEmail, String verificationCode) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email Verification - Deshi Store");
            
            String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px; background-color: #f5f5f5;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                "<h2 style='color: #2E7D32; margin-bottom: 20px;'>Email Verification</h2>" +
                "<p style='font-size: 16px; color: #333; margin-bottom: 20px;'>Your verification code is:</p>" +
                "<div style='background-color: #E8F5E9; padding: 20px; border-radius: 5px; text-align: center; margin-bottom: 20px;'>" +
                "<h1 style='color: #2E7D32; margin: 0; font-size: 36px; letter-spacing: 5px;'>" + verificationCode + "</h1>" +
                "</div>" +
                "<p style='font-size: 14px; color: #666; margin-bottom: 10px;'>This code will expire in 10 minutes.</p>" +
                "<p style='font-size: 14px; color: #666;'>If you didn't request this code, please ignore this email.</p>" +
                "<hr style='border: none; border-top: 1px solid #e0e0e0; margin: 20px 0;'>" +
                "<p style='font-size: 12px; color: #999; text-align: center;'>Finding BD Products - Deshi Store</p>" +
                "</div>" +
                "</body>" +
                "</html>";
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            System.out.println("Verification email sent successfully to: " + toEmail);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
