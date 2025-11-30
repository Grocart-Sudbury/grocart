package com.grocart.grocart.Controller;

import com.grocart.grocart.DTO.CustomerOrderResponseDTO;
import com.grocart.grocart.DTO.CustomerOrderItemResponseDTO;
import com.grocart.grocart.DTO.CustomerProductResponseDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Map<String, Object> getUserByEmail(@RequestParam String email) {
        Customer customer = customerService.getCustomerByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = customerService.getOrdersByEmail(email);

        // Map orders to DTO with full details
        List<CustomerOrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToCustomerOrderResponseDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("firstName", customer.getFirstName());
        response.put("lastName", customer.getLastName());
        response.put("phone", customer.getPhone());
        response.put("address", customer.getAddress());
        response.put("orders", orderDTOs);

        return response;
    }

    private CustomerOrderResponseDTO convertToCustomerOrderResponseDTO(Order order) {
        CustomerOrderResponseDTO dto = new CustomerOrderResponseDTO();

        // Basic order info
        dto.setId(order.getId());
        dto.setTrackingId(order.getTrackingId());

        // Customer/Shipping info
        dto.setFirstName(order.getFirstName());
        dto.setLastName(order.getLastName());
        dto.setEmail(order.getEmail());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setCity(order.getCity());
        dto.setProvince(order.getProvince());
        dto.setPostalCode(order.getPostalCode());

        // Totals
        dto.setSubtotal(order.getSubtotal());
        dto.setTax(order.getTax());
        dto.setDiscount(order.getDiscount());
        dto.setTotal(order.getTotal());

        // Status and timestamps
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        // Map order items
        if (order.getItems() != null) {
            List<CustomerOrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(this::convertToCustomerOrderItemResponseDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    private CustomerOrderItemResponseDTO convertToCustomerOrderItemResponseDTO(com.grocart.grocart.Entities.OrderItem item) {
        CustomerOrderItemResponseDTO dto = new CustomerOrderItemResponseDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());

        // Map product details
        if (item.getProduct() != null) {
            dto.setProduct(convertToCustomerProductResponseDTO(item.getProduct()));
        }

        return dto;
    }

    private CustomerProductResponseDTO convertToCustomerProductResponseDTO(com.grocart.grocart.Entities.Product product) {
        CustomerProductResponseDTO dto = new CustomerProductResponseDTO();
        dto.setId(product.getId());
        dto.setProduct(product.getProduct());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setOfferPrice(product.getOfferPrice());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setQuantity(product.getQuantity());
        dto.setImageUrl(product.getImageUrl());

        // Set category name only (to avoid lazy loading issues)
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }

        return dto;
    }
}