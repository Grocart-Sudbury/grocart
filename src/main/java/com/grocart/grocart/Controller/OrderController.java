package com.grocart.grocart.Controller;

import com.grocart.grocart.DTO.OrderDTO;
import com.grocart.grocart.DTO.OrderResponseDTO;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Services.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/by-date")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDate(date);
        return ResponseEntity.ok(orders);
    }
}
