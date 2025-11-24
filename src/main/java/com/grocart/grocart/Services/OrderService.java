package com.grocart.grocart.Services;

import com.grocart.grocart.DTO.*;
import com.grocart.grocart.Entities.Customer;
import com.grocart.grocart.Entities.Order;
import com.grocart.grocart.Entities.OrderItem;
import com.grocart.grocart.Entities.Product;
import com.grocart.grocart.Repository.CustomerRepository;
import com.grocart.grocart.Repository.OrderItemRepository;
import com.grocart.grocart.Repository.OrderRepository;
import com.grocart.grocart.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<TopOrderedProductDTO> getTopOrderedProducts() {
        return orderItemRepository.findTopOrderedProductsFull();
    }

    public List<OrderResponseDTO> getOrdersByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        return orders.stream().map(order -> new OrderResponseDTO(
                order.getId(),
                order.getFirstName(),
                order.getLastName(),
                order.getEmail(),
                order.getPhone(),
                order.getAddress(),
                order.getCity(),
                order.getProvince(),
                order.getPostalCode(),
                order.getSubtotal(),
                order.getTax(),
                order.getDiscount(),
                order.getTotal(),
                order.getStatus(),
                order.getItems().stream().map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getProduct().getProduct() // product name only
                )).toList()
        )).toList();
    }

    public Order findByTrackingId(String trackingId) {
        return orderRepository.findByTrackingId(trackingId);
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // Check if customer exists
        Customer customer = customerRepository.findByEmail(orderDTO.getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer(
                            orderDTO.getEmail(),
                            orderDTO.getFirstName(),
                            orderDTO.getLastName(),
                            orderDTO.getPhone(),
                            orderDTO.getAddress()
                    );
                    return customerRepository.save(newCustomer);
                });

        Order order = new Order();
        order.setFirstName(customer.getFirstName());
        order.setLastName(customer.getLastName());
        order.setEmail(customer.getEmail());
        order.setPhone(customer.getPhone());
        order.setAddress(customer.getAddress());
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

        order.setTrackingId("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setItems(orderItems);
        order.setSubtotal(subtotal);
        order.setTax(subtotal * 0.13); // 13% tax
        order.setTotal(subtotal + (subtotal * 0.13) - order.getDiscount());

        return orderRepository.save(order);
    }
}
