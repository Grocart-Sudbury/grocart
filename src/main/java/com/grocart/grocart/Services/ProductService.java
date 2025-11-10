package com.grocart.grocart.Services;

import com.grocart.grocart.Entities.Product;
import com.grocart.grocart.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    public Map<String, List<Product>> getProductsGroupedByDiscount() {
        List<Product> products = productRepository.findAll();

        // Group products by rounded discount percentage
        Map<String, List<Product>> grouped = products.stream()
                .filter(p -> p.getOriginalPrice() != null && p.getOfferPrice() != null && p.getOriginalPrice() > 0)
                .collect(Collectors.groupingBy(p -> {
                    double discount = ((p.getOriginalPrice() - p.getOfferPrice()) / p.getOriginalPrice()) * 100;
                    int rounded = ((int) Math.round(discount / 10)) * 10; // round to nearest 10
                    return rounded + "% off";
                }));

        // Sort by discount descending (e.g. 70% off → 60% off → 50% off)
        return grouped.entrySet().stream()
                .sorted((e1, e2) -> {
                    int d1 = Integer.parseInt(e1.getKey().replace("% off", "").trim());
                    int d2 = Integer.parseInt(e2.getKey().replace("% off", "").trim());
                    return Integer.compare(d2, d1); // descending
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
