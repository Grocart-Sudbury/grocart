package com.grocart.grocart.DTO;

public class CustomerOrderItemResponseDTO {

    private Long id;
    private Integer quantity;
    private Double priceAtPurchase;

    // Product details embedded
    private CustomerProductResponseDTO product;

    public CustomerOrderItemResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(Double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public CustomerProductResponseDTO getProduct() {
        return product;
    }

    public void setProduct(CustomerProductResponseDTO product) {
        this.product = product;
    }
}