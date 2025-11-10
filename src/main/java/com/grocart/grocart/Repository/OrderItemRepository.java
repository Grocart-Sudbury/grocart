package com.grocart.grocart.Repository;

import com.grocart.grocart.DTO.TopOrderedProductDTO;
import com.grocart.grocart.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT new com.grocart.grocart.DTO.TopOrderedProductDTO(
            p.id,
            p.product,
            p.originalPrice,
            p.offerPrice,
            p.description,
            p.stock,
            p.quantity,
            p.imageUrl,
            SUM(oi.quantity)
        )
        FROM OrderItem oi
        JOIN oi.product p
        GROUP BY p.id, p.product, p.originalPrice, p.offerPrice, p.description, p.stock, p.quantity, p.imageUrl
        ORDER BY SUM(oi.quantity) DESC
        """)
    List<TopOrderedProductDTO> findTopOrderedProductsFull();
}