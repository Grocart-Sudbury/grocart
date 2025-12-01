package com.grocart.grocart.Controller;


import com.grocart.grocart.DTO.OrderResponseDTO;
import com.grocart.grocart.DTO.OrderResponseStatusDTO;
import com.grocart.grocart.DTO.OrderStatusDTO;
import com.grocart.grocart.Services.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orderstatus")

public class OrderStatusController {

    @Autowired
    private OrderStatusService orderStatusService;

    /**
     * Update order status by order ID
     * PATCH /api/orders/{id}/status
     * Body: { "status": "OutForDelivery" }
     *
     * Returns OrderResponseDTO to avoid lazy loading serialization issues
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusDTO statusUpdate) {
        try {
            String newStatus = statusUpdate.getStatus();

            if (newStatus == null || newStatus.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            OrderResponseStatusDTO orderDTO = orderStatusService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok(orderDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }
}