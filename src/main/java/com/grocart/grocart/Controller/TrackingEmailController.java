package com.grocart.grocart.Controller;



import com.grocart.grocart.Services.EmailService;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")

public class TrackingEmailController {

    @Autowired
    private EmailService emailService;

    // ----------------------------------------------------------------------
    // ✅ Send Tracking Email Endpoint
    // ----------------------------------------------------------------------
    @PostMapping("/send-tracking")
    public ResponseEntity<Map<String, String>> sendTrackingEmail(
            @RequestBody TrackingEmailRequest request) {

        Map<String, String> response = new HashMap<>();

        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Email address is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getTrackingId() == null || request.getTrackingId().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Tracking ID is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
                response.put("status", "error");
                response.put("message", "First name is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Send the email
            emailService.sendTrackingEmail(
                    request.getEmail(),
                    request.getTrackingId(),
                    request.getFirstName()
            );

            response.put("status", "success");
            response.put("message", "Tracking email sent successfully to " + request.getEmail());
            return ResponseEntity.ok(response);

        } catch (PostmarkException e) {
            response.put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Network error while sending email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ----------------------------------------------------------------------
    // ✅ Request DTO Class
    // ----------------------------------------------------------------------
    public static class TrackingEmailRequest {
        private String email;
        private String trackingId;
        private String firstName;

        // Constructors
        public TrackingEmailRequest() {}

        public TrackingEmailRequest(String email, String trackingId, String firstName) {
            this.email = email;
            this.trackingId = trackingId;
            this.firstName = firstName;
        }

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTrackingId() {
            return trackingId;
        }

        public void setTrackingId(String trackingId) {
            this.trackingId = trackingId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
}
