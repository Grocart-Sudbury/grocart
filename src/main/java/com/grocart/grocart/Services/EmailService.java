package com.grocart.grocart.Services;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    // 🔹 API client at class level (reusable)
    private final ApiClient client;

    public EmailService() {
        this.client = Postmark.getApiClient("c535b170-745e-4cce-babb-c3456b31204f");
    }

    // ----------------------------------------------------------------------
    // ✅ 1. Send Tracking Email
    // ----------------------------------------------------------------------
    public void sendTrackingEmail(String to, String trackingId, String firstName)
            throws PostmarkException, IOException {

        String subject = "Your Order Tracking Information";

        String htmlBody = """
            <html>
                <body>
                    <h2 style="color:#4CAF50;">Hi %s,</h2>
                    <p>Your order has been placed successfully!</p>
                    <p><b>Tracking ID:</b> %s</p>
                    <p>You can use this ID to track your shipment.</p>
                    <br/>
                    <p>Thank you for shopping with us! 😊</p>
                </body>
            </html>
        """.formatted(firstName, trackingId);

        Message message = new Message(
                "zeeza@grocartinc.ca",
                to,
                subject,
                null
        );

        message.setHtmlBody(htmlBody);
        message.setTextBody("Your tracking ID is: " + trackingId);

        MessageResponse response = client.deliverMessage(message);

        System.out.println("Email sent: " + response.getMessage());
    }

    // ----------------------------------------------------------------------
    // ✅ 2. Send OTP Email
    // ----------------------------------------------------------------------
    public void sendOtpEmail(String to, String otp)
            throws PostmarkException, IOException {

        String subject = "Your Grocart Login OTP";

        String htmlBody = """
            <html>
                <body>
                    <h2 style="color:#4CAF50;">Your OTP Code</h2>
                    <p>Your One-Time Password for logging in is:</p>
                    <h1 style="color:#2E86C1;">%s</h1>
                    <p>This OTP will expire in 5 minutes.</p>
                    <br/>
                    <p>Thank you for using Grocart!</p>
                </body>
            </html>
        """.formatted(otp);

        Message message = new Message(
                "zeeza@grocartinc.ca",
                to,
                subject,
                null
        );

        message.setHtmlBody(htmlBody);
        message.setTextBody("Your OTP is: " + otp);

        MessageResponse response = client.deliverMessage(message);

        System.out.println("OTP Email sent: " + response.getMessage());
    }
}
