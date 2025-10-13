package com.grocart.grocart.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product; // product name

    private Double originalPrice; // original price before offer
    private Double offerPrice; // discounted price
    private String description; // product description
    private Integer stock; // total stock available
    private String quantity; // quantity per package or unit
    private String imageUrl; // image URL

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product() {}

    public Product(String product,  Double originalPrice, Double offerPrice,
                   String description, Integer stock, String quantity, String imageUrl,
                   Category category) {
        this.product = product;

        this.originalPrice = originalPrice;
        this.offerPrice = offerPrice;
        this.description = description;
        this.stock = stock;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and Setters
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product='" + product + '\'' +

                ", originalPrice=" + originalPrice +
                ", offerPrice=" + offerPrice +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + (category != null ? category.getName() : null) +
                '}';
    }
}
