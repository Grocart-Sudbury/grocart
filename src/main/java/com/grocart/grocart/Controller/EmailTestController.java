package com.grocart.grocart.Controller;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmailTestController {

    @Value("${postmark.api-key}")
    private String serverToken;

    @Value("${postmark.from-email}")
    private String senderSignature;

    @GetMapping("/send-test-email")
    public String sendTestEmail(@RequestParam String to) throws PostmarkException, IOException {

        // Create ApiClient
        ApiClient client = Postmark.getApiClient("c535b170-745e-4cce-babb-c3456b31204f");

        // Create HTML message
        Message message = new Message(
                "zeeza@grocartinc.ca",     // From (must be verified in Postmark)
                to,                        // To
                "Hello from Postmark!",    // Subject
                null                       // Plain text body (optional)
        );

        // Set HTML body
        message.setHtmlBody("""
        <html>
            <body>
                <h1 style="color:#4CAF50;">Welcome!</h1>
                <p>This is a <b>test HTML email</b> sent using Postmark.</p>
                <p>Thanks for trying this out 🚀</p>
            </body>
        </html>
    """);

        // Optionally, also include a fallback plain text body
        message.setTextBody("This is the text fallback for clients that don't support HTML.");

        // Send the message
        MessageResponse response = client.deliverMessage(message);

        return "Email sent successfully: " + response.getMessage();
    }
}
