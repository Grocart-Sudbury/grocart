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



            // 2. Create ApiClient with base URL and headers
            ApiClient client = Postmark.getApiClient("c535b170-745e-4cce-babb-c3456b31204f");



            // 4. Create message using constructor you specified
            Message message = new Message(
                    "zeeza@grocartinc.ca",       // Verified sender signature
                    to,                    // Recipient email
                    "Hello from Postmark!",// Subject
                    "Hello message body"   // Plain text body
            );

            MessageResponse response = client.deliverMessage(message);

            return "Email sent successfully: " + response.getMessage();


    }
}
