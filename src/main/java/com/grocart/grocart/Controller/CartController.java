package com.grocart.grocart.Controller;


import com.grocart.grocart.Entities.CartRequest;
import com.grocart.grocart.Entities.PriceBreakdown;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @PostMapping("/pricing")
    public PriceBreakdown calculate(@RequestBody CartRequest request) {

        double subtotal = request.getCart().stream()
                .mapToDouble(item -> item.getOfferPrice() * item.getQuantity())
                .sum();

        double tax = subtotal * 0.13;
        double shipping = 6.99;
        double serviceFee = 3.50;
        double discount = 0;

        if ("SAVE10".equalsIgnoreCase(request.getCouponCode())) {
            discount = subtotal * 0.10;
        }

        double total = subtotal + tax + shipping + serviceFee - discount;

        return new PriceBreakdown(subtotal, tax, shipping, serviceFee, discount, total);
    }
}
