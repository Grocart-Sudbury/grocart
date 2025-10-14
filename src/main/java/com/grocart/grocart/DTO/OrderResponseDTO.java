package com.grocart.grocart.DTO;

import java.util.List;

public record OrderResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String province,
        String postalCode,
        Double subtotal,
        Double tax,
        Double discount,
        Double total,
        String status,
        List<OrderItemResponseDTO> items
) {}
