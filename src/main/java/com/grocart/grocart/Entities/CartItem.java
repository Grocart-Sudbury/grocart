package com.grocart.grocart.Entities;

public class CartItem {

    private Long id;
    private String product;
    private Double originalPrice;
    private Double offerPrice;
    private String description;
    private Integer stock;
    private String quantity;        // can ignore
    private String imageUrl;
    private String category;
    private Integer quantityInCart;

    public CartItem() {
    }

    public CartItem(Long id, String product, Double originalPrice, Double offerPrice,
                    String description, Integer stock, String quantity,
                    String imageUrl, String category, Integer quantityInCart) {
        this.id = id;
        this.product = product;
        this.originalPrice = originalPrice;
        this.offerPrice = offerPrice;
        this.description = description;
        this.stock = stock;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.category = category;
        this.quantityInCart = quantityInCart;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(Integer quantityInCart) {
        this.quantityInCart = quantityInCart;
    }
}
