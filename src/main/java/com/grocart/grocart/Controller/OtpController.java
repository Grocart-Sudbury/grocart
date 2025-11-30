package com.grocart.grocart.Controller;

import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Repository.CustomerRepository;
import com.grocart.grocart.Services.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final EmailService emailService;

    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, ResendTracker> resendTrackers = new ConcurrentHashMap<>();

    private static final long OTP_EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes
    private static final long RESEND_RESET_MS = 5 * 60 * 1000; // 5 minutes - reset resend limit
    private static final int MAX_RESEND = 3;

    private final CustomerRepository customerRepository;

    public OtpController(EmailService emailService, CustomerRepository customerRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
    }

    // ------------------- Send OTP -------------------
    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {
        long now = System.currentTimeMillis();

        // Clean up expired resend tracker
        ResendTracker tracker = resendTrackers.get(email);
        if (tracker != null && now - tracker.firstAttemptTime > RESEND_RESET_MS) {
            resendTrackers.remove(email);
            tracker = null;
        }

        // Initialize or get existing tracker
        if (tracker == null) {
            tracker = new ResendTracker(now);
            resendTrackers.put(email, tracker);
        }

        // Check if max resend limit reached
        if (tracker.count >= MAX_RESEND) {
            long timeRemaining = RESEND_RESET_MS - (now - tracker.firstAttemptTime);
            long minutesRemaining = timeRemaining / (60 * 1000);
            return "You have reached the maximum number of OTP requests. Please try again in "
                    + minutesRemaining + " minute(s).";
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        try {
            emailService.sendOtpEmail(email, otp);

            otpStorage.put(email, new OtpEntry(otp, now));
            tracker.count++;

            return "OTP sent to " + email;
        } catch (Exception e) {
            return "Error sending OTP: " + e.getMessage();
        }
    }

    // ------------------- Verify OTP -------------------
    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        OtpEntry entry = otpStorage.get(email);

        if (entry == null) {
            return "No OTP found for this email. Please request a new one.";
        }

        long now = System.currentTimeMillis();
        if (now - entry.timestamp > OTP_EXPIRATION_MS) {
            otpStorage.remove(email);
            return "OTP has expired. Please request a new one.";
        }

        if (entry.otp.equals(otp)) {
            otpStorage.remove(email);
            resendTrackers.remove(email); // reset count after success

            // ✅ Create customer if not exists
            customerRepository.findByEmail(email).orElseGet(() -> {
                Customer customer = new Customer();
                customer.setEmail(email);
                customer.setFirstName(""); // optional default
                customer.setLastName("");  // optional default
                customer.setPhone("");
                customer.setAddress("");
                return customerRepository.save(customer);
            });

            return "OTP verified successfully!";
        } else {
            return "Invalid OTP. Please try again.";
        }
    }

    // ------------------- Internal classes -------------------
    private static class OtpEntry {
        String otp;
        long timestamp;

        OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }

    private static class ResendTracker {
        int count;
        long firstAttemptTime;

        ResendTracker(long firstAttemptTime) {
            this.count = 0;
            this.firstAttemptTime = firstAttemptTime;
        }
    }
}