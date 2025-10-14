package com.grocart.grocart.DTO;

import java.util.List;

// Response DTO for OrderItem
public record OrderItemResponseDTO(
        Long id,
        Integer quantity,
        Double priceAtPurchase,
        String productName
) {}


