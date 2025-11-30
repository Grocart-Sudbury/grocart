package com.grocart.grocart.Controller;

import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Services.CustomerLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class CustomerLoginController {

    private final CustomerLoginService customerLoginService;

    public CustomerLoginController(CustomerLoginService customerLoginService) {
        this.customerLoginService = customerLoginService;
    }

    /**
     * Login a customer
     * POST /api/login
     * Request body: { "email": "user@example.com", "password": "password123" }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> loginCustomer(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");

            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                response.put("error", "Email is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (password == null || password.trim().isEmpty()) {
                response.put("error", "Password is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Authenticate customer
            Customer customer = customerLoginService.authenticateCustomer(email, password);

            // Build success response
            response.put("message", "Login successful");
            response.put("id", customer.getId());
            response.put("email", customer.getEmail());
            response.put("firstName", customer.getFirstName());
            response.put("lastName", customer.getLastName());
            response.put("phone", customer.getPhone());
            response.put("address", customer.getAddress());
            // Note: Password is NOT included in response for security

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Invalid credentials or validation error
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            // Unexpected error
            response.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Logout a customer (optional endpoint for future session management)
     * POST /api/login/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutCustomer() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");

        // In a real application with sessions/tokens, you would invalidate them here

        return ResponseEntity.ok(response);
    }
}