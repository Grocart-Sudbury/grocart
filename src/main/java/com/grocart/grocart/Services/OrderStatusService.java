package com.grocart.grocart.Services;


import com.grocart.grocart.DTO.OrderResponseDTO;
import com.grocart.grocart.DTO.OrderResponseStatusDTO;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderStatusService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Update order status by order ID
     * Returns DTO to avoid lazy loading serialization issues
     */
    @Transactional
    public OrderResponseStatusDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Validate status
        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus + ". Valid statuses are: Pending, OutForDelivery, Completed");
        }

        // Validate status transition
        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);

        // Convert to DTO before returning
        return new OrderResponseStatusDTO(savedOrder);
    }

    /**
     * Validate if status is valid
     */
    private boolean isValidStatus(String status) {
        return status.equals("Pending") ||
                status.equals("OutForDelivery") ||
                status.equals("Completed");
    }

    /**
     * Validate status transition logic
     * Pending -> OutForDelivery -> Completed
     */
    private void validateStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals("Completed")) {
            throw new IllegalStateException("Cannot change status of a completed order");
        }

        if (currentStatus.equals("Pending") && newStatus.equals("Completed")) {
            throw new IllegalStateException("Order must be 'OutForDelivery' before it can be 'Completed'");
        }

        if (currentStatus.equals("OutForDelivery") && newStatus.equals("Pending")) {
            throw new IllegalStateException("Cannot move order back to 'Pending' from 'OutForDelivery'");
        }
    }
}