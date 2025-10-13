package com.grocart.grocart.Controller;

import com.grocart.grocart.DTO.LoginRequest;
import com.grocart.grocart.DTO.LoginResponse;
import com.grocart.grocart.Entities.User;
import com.grocart.grocart.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Use BCrypt to match passwords
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new LoginResponse("Login successful", true);
            } else {
                return new LoginResponse("Invalid password", false);
            }
        } else {
            return new LoginResponse("User not found", false);
        }
    }
}
