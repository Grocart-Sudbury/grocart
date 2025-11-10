package com.grocart.grocart.Controller;

import com.grocart.grocart.DTO.*;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Services.EmailService;
import com.grocart.grocart.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private EmailService emailService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.createOrder(orderDTO);
        // Send email
        try {
            emailService.sendTrackingEmail(
                    order.getEmail(),
                    order.getTrackingId(),
                    order.getFirstName()
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send tracking email");
        }
        return ResponseEntity.ok(order);
    }
    @GetMapping("/top-products")
    public ResponseEntity<List<TopOrderedProductDTO>> getTopOrderedProducts() {
        List<TopOrderedProductDTO> topProducts = orderService.getTopOrderedProducts();
        return ResponseEntity.ok(topProducts);
    }
    @GetMapping("/by-date")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDate(date);
        return ResponseEntity.ok(orders);
    }
    // ✅ Add tracking endpoint here
    @GetMapping("/track/{trackingId}")
    public ResponseEntity<OrderTrackingDTO> trackOrder(@PathVariable String trackingId) {
        Order order = orderService.findByTrackingId(trackingId);
        if (order == null) return ResponseEntity.notFound().build();

        List<OrderItemDTO> items = order.getItems().stream().map(i -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setProductId(i.getProduct().getId());
            dto.setQuantity(i.getQuantity());

            return dto;
        }).toList();

        OrderTrackingDTO dto = new OrderTrackingDTO();
        dto.setTrackingId(order.getTrackingId());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setItems(items);

        return ResponseEntity.ok(dto);
    }
}
