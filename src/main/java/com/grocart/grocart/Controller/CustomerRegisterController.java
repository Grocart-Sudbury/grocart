package com.grocart.grocart.Controller;

import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Services.CustomerRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class CustomerRegisterController {

    private final CustomerRegisterService customerRegisterService;

    public CustomerRegisterController(CustomerRegisterService customerRegisterService) {
        this.customerRegisterService = customerRegisterService;
    }

    /**
     * Register a new customer
     * POST /api/register
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerCustomer(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");
            String firstName = request.get("firstName");
            String lastName = request.get("lastName");
            String phone = request.get("phone");
            String address = request.get("address");

            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                response.put("error", "Email is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (password == null || password.trim().isEmpty()) {
                response.put("error", "Password is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                response.put("error", "First name is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                response.put("error", "Last name is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Customer customer = customerRegisterService.registerCustomer(
                    email, password, firstName, lastName, phone, address
            );

            response.put("message", "Customer registered successfully");
            response.put("id", customer.getId());
            response.put("email", customer.getEmail());
            response.put("firstName", customer.getFirstName());
            response.put("lastName", customer.getLastName());
            response.put("phone", customer.getPhone());
            response.put("address", customer.getAddress());
            // Note: Password is NOT included in response for security

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception e) {
            response.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Check if email is available
     * GET /api/register/check-email?email=someone@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(@RequestParam String email) {
        boolean available = customerRegisterService.isEmailAvailable(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);

        return ResponseEntity.ok(response);
    }
}