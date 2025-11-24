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
    private final Map<String, Integer> resendCount = new ConcurrentHashMap<>();

    private static final long OTP_EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes
    private static final int MAX_RESEND = 3;


    private final CustomerRepository customerRepository;



    public OtpController(EmailService emailService, CustomerRepository customerRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
    }
    // ------------------- Send OTP -------------------
    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {
        int count = resendCount.getOrDefault(email, 0);
        if (count >= MAX_RESEND) {
            return "You have reached the maximum number of OTP requests. Please try again later.";
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        try {
            emailService.sendOtpEmail(email, otp);

            otpStorage.put(email, new OtpEntry(otp, System.currentTimeMillis()));
            resendCount.put(email, count + 1);

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
            resendCount.remove(email); // reset count after success

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

    // ------------------- Internal class for OTP -------------------
    private static class OtpEntry {
        String otp;
        long timestamp;

        OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
}
