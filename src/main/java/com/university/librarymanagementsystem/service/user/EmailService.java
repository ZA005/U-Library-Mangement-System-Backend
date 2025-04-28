package com.university.librarymanagementsystem.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String emailAdd, String action, String bookTitle, String dueDate) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailAdd);
            helper.setSubject("University Library Management System - " + action + " Notification");

            // Prepare due date section (only for "Borrowed" and "Renewed" actions)
            String dueDateSection = "";
            if ("Borrowed".equalsIgnoreCase(action) || "Renewed".equalsIgnoreCase(action)) {
                dueDateSection = "<p><strong>Due Date:</strong> " + dueDate + "</p>";
            }
            if ("Reserved".equalsIgnoreCase(action)) {
                dueDateSection = "<p><strong>Expiration Date:</strong> " + dueDate + "</p>";
            }
            // Define action message
            String actionMessage;
            String followUpMessage;

            switch (action) {
                case "Borrowed":
                    actionMessage = "You have successfully borrowed the book:";
                    followUpMessage = "Please ensure you return it by the due date.";
                    break;
                case "Returned":
                    actionMessage = "You have successfully returned the book:";
                    followUpMessage = "";
                    break;
                case "Renewed":
                    actionMessage = "You have successfully renewed the book:";
                    followUpMessage = "Please note your new due date.";
                    break;
                case "Reserved":
                    actionMessage = "You have successfully reserved the book:";
                    followUpMessage = "You will be notified via email once the book becomes available.y";
                    break;
                case "Notify":
                    actionMessage = "The book you reserved is now available for pickup:";
                    followUpMessage = "Please visit the library to collect your book.";
                    break;
                default:
                    actionMessage = "This is a notification regarding your book:";
                    followUpMessage = "";
            }

            // Load HTML template and replace placeholders
            String emailContent = EMAIL_TEMPLATE
                    .replace("{{ACTION_MESSAGE}}", actionMessage)
                    .replace("{{BOOK_TITLE}}", bookTitle)
                    .replace("{{DUE_DATE_SECTION}}", dueDateSection)
                    .replace("{{FOLLOWUP_MESSAGE}}", followUpMessage)
                    .replace("{{LIBRARY_URL}}", "http://localhost:5173/");

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendOTPEmail(String email, String otp) {
        System.out.println("RUN5" + email);
        System.out.println("RUN6" + otp);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            System.out.println("RUN7");

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            System.out.println("RUN8");

            helper.setTo(email);
            System.out.println("RUN9");

            helper.setSubject("University Library Management System - OTP Verification");
            System.out.println("RUN10 ");

            String emailContent = OTP_EMAIL_TEMPLATE.replace("{{OTP_CODE}}", otp);
            System.out.println("RUN11 ");

            helper.setText(emailContent, true);
            System.out.println("RUN12 ");

            mailSender.send(mimeMessage);
            System.out.println("RUN13 ");

        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }
    }

    private static final String OTP_EMAIL_TEMPLATE = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>OTP Verification</title>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px;
                            border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }
                        .header { background-color: #EA4040; color: white; padding: 10px; text-align: center;
                            font-size: 20px; font-weight: bold; border-radius: 8px 8px 0 0; }
                        .content { padding: 20px; font-size: 16px; color: #333; }
                        .otp { font-size: 24px; font-weight: bold; color: #EA4040; }
                        .footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px;
                            padding-top: 10px; border-top: 1px solid #ddd; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">University Library Management System</div>
                        <div class="content">
                            <p>Dear User,</p>
                            <p>Here is your One-Time Password (OTP) for secure access to your account:</p>
                            <p class="otp">{{OTP_CODE}}</p>
                            <p>Please enter this code to activate your account. This OTP is valid for a limited time.</p>
                            <p>If you did not request this OTP, please contact our support team immediately.</p>
                            <p>Best regards,</p>
                            <p><strong>University Library Management System</strong></p>
                            <p>University of Nueva Caceres</p>
                        </div>
                        <div class="footer">
                            Note: This email is sent from a no-reply address. Do not reply to this email.
                            For support, please visit our website or call our support team.
                        </div>
                    </div>
                </body>
                </html>
            """;

    private static final String EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Library Notification</title>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px;
                        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }
                    .header { background-color: #EA4040; color: white; padding: 10px; text-align: center; font-size: 20px;
                        font-weight: bold; border-radius: 8px 8px 0 0; }
                    .content { padding: 20px; font-size: 16px; color: #333; }
                    .highlight { font-weight: bold; color: #EA4040; }
                    .footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; padding-top: 10px;
                        border-top: 1px solid #ddd; }
                    .button { display: inline-block; padding: 10px 15px; margin-top: 15px; background-color: #EA4040; color: white;
                        text-decoration: none; border-radius: 5px; font-weight: bold; }
                    .button:hover { background-color: #c93030; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">University Library Management System</div>
                    <div class="content">
                        <p>Dear User,</p>

                        <p>
                            {{ACTION_MESSAGE}}
                        </p>

                        <p><strong>Book Title:</strong> {{BOOK_TITLE}}</p>

                        {{DUE_DATE_SECTION}}

                        <p>
                            {{FOLLOWUP_MESSAGE}}
                        </p>

                        <a href="{{LIBRARY_URL}}" class="button">Visit Library</a>

                        <p>If you believe this is an error, please contact our support team immediately.</p>

                        <p>Best regards,</p>
                        <p><strong>University Library Management System</strong></p>
                        <p>University of Nueva Caceres</p>
                    </div>
                    <div class="footer">
                        Note: This email is sent from a no-reply address. Do not reply to this email.<br>
                        For support, please visit our website or call our support team.
                    </div>
                </div>
            </body>
            </html>
            """;
}