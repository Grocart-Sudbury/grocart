package com.grocart.grocart.Services;

import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerRegisterService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerRegisterService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Register a new customer with encrypted password
     * @param email Customer email
     * @param password Customer password (will be encrypted)
     * @param firstName Customer first name
     * @param lastName Customer last name
     * @param phone Customer phone number
     * @param address Customer address
     * @return Saved customer entity
     * @throws IllegalArgumentException if email already exists or password is weak
     */
    @Transactional
    public Customer registerCustomer(String email, String password, String firstName,
                                     String lastName, String phone, String address) {
        // Check if email already exists
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        // Validate password strength
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(password);

        // Create new customer with encrypted password
        Customer customer = new Customer(email, encryptedPassword, firstName, lastName, phone, address);

        // Save and return
        return customerRepository.save(customer);
    }

    /**
     * Check if email is available for registration
     * @param email Email to check
     * @return true if available, false if already taken
     */
    public boolean isEmailAvailable(String email) {
        return customerRepository.findByEmail(email).isEmpty();
    }

    /**
     * Get customer by email
     * @param email Customer email
     * @return Customer if found
     * @throws RuntimeException if customer not found
     */
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }

    /**
     * Verify customer password
     * @param email Customer email
     * @param rawPassword Plain text password to verify
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String email, String rawPassword) {
        Customer customer = getCustomerByEmail(email);
        return passwordEncoder.matches(rawPassword, customer.getPassword());
    }
}