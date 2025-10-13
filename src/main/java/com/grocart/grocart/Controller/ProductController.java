package com.grocart.grocart.Controller;

import com.grocart.grocart.Entities.Category;
import com.grocart.grocart.Entities.Product;
import com.grocart.grocart.Services.CategoryService;
import com.grocart.grocart.Services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // Constructor injection for both services
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryService.getCategoryById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Update existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.getProductById(id)
                .map(existingProduct -> {
                    // Update basic fields
                    existingProduct.setProduct(productDetails.getProduct());
                    existingProduct.setOfferPrice(productDetails.getOfferPrice());
                    existingProduct.setOriginalPrice(productDetails.getOriginalPrice());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setStock(productDetails.getStock());
                    existingProduct.setQuantity(productDetails.getQuantity());
                    existingProduct.setImageUrl(productDetails.getImageUrl());

                    // Update category safely
                    if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
                        Category category = categoryService.getCategoryById(productDetails.getCategory().getId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        existingProduct.setCategory(category);
                    }

                    // Save updated product
                    Product updatedProduct = productService.saveProduct(existingProduct);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
