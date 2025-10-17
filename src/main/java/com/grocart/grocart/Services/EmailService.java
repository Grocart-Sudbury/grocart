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
    public void sendTrackingEmail(String to, String trackingId, String firstName)
            throws PostmarkException, IOException {

        ApiClient client = Postmark.getApiClient("c535b170-745e-4cce-babb-c3456b31204f");

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
}
