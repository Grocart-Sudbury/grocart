package com.grocart.grocart.Controller;

import com.grocart.grocart.DTO.OrderDTO;
import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /api/user?email=someone@example.com
    @GetMapping
    public Map<String, Object> getUserByEmail(@RequestParam String email) {
        Customer customer = customerService.getCustomerByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = customerService.getOrdersByEmail(email);

        // Map orders to DTO
        List<OrderDTO> orderDTOs = orders.stream()
                .map(o -> new OrderDTO(
                        o.getTrackingId()

                ))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("firstName", customer.getFirstName());
        response.put("lastName", customer.getLastName());
        response.put("phone", customer.getPhone());
        response.put("address", customer.getAddress());
        response.put("orders", orderDTOs);

        return response;
    }
}
