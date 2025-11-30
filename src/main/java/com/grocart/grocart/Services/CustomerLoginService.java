package com.grocart.grocart.Services;

import com.grocart.grocart.Entities.Customer;

import com.grocart.grocart.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerLoginService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerLoginService(CustomerRepository customerRepository,
                                PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticate a customer with email and password
     * @param email Customer's email
     * @param password Customer's password (plain text)
     * @return Customer object if authentication successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public Customer authenticateCustomer(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        Optional<Customer> customerOptional = customerRepository.findByEmail(email.trim().toLowerCase());

        if (customerOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Customer customer = customerOptional.get();

        // Verify password
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return customer;
    }

    /**
     * Find customer by email
     * @param email Customer's email
     * @return Optional containing customer if found
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email.trim().toLowerCase());
    }
}