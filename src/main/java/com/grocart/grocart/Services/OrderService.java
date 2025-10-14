package com.grocart.grocart.Services;

import com.grocart.grocart.DTO.OrderDTO;
import com.grocart.grocart.DTO.OrderItemDTO;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Entities.OrderItem;
import com.grocart.grocart.Entities.Product;

import com.grocart.grocart.Repository.OrderRepository;
import com.grocart.grocart.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setFirstName(orderDTO.getFirstName());
        order.setLastName(orderDTO.getLastName());
        order.setEmail(orderDTO.getEmail());
        order.setPhone(orderDTO.getPhone());
        order.setAddress(orderDTO.getAddress());
        order.setCity(orderDTO.getCity());
        order.setProvince(orderDTO.getProvince());
        order.setPostalCode(orderDTO.getPostalCode());
        order.setDiscount(orderDTO.getDiscount() != null ? orderDTO.getDiscount() : 0.0);

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPriceAtPurchase(product.getOfferPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            subtotal += product.getOfferPrice() * itemDTO.getQuantity();
        }

        order.setItems(orderItems);
        order.setSubtotal(subtotal);
        order.setTax(subtotal * 0.13); // 13% tax
        order.setTotal(subtotal + (subtotal * 0.13) - order.getDiscount());

        return orderRepository.save(order);
    }
}