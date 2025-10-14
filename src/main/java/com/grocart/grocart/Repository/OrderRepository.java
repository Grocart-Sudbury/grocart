package com.grocart.grocart.Repository;

import com.grocart.grocart.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // You can add custom queries here if needed, e.g., find by status or customer email
}
