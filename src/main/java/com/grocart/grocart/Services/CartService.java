package com.grocart.grocart.Services;


import com.grocart.grocart.Entities.CartItem;
import com.grocart.grocart.Entities.CartRequest;
import com.grocart.grocart.Entities.PriceBreakdown;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private static final double TAX_RATE = 0.13;
    private static final double SHIPPING_FEE = 6.99;
    private static final double SERVICE_FEE = 3.50;

    public PriceBreakdown calculatePrice(CartRequest request) {
        double subtotal = request.getCart().stream()
                .mapToDouble(item -> item.getOfferPrice() * item.getQuantity())
                .sum();

        double tax = subtotal * TAX_RATE;
        double shipping = SHIPPING_FEE;
        double serviceFee = SERVICE_FEE;
        double discount = calculateDiscount(subtotal, request.getCouponCode());

        double total = subtotal + tax + shipping + serviceFee - discount;

        return new PriceBreakdown(subtotal, tax, shipping, serviceFee, discount, total);
    }

    private double calculateDiscount(double subtotal, String couponCode) {
        if ("SAVE10".equalsIgnoreCase(couponCode)) {
            return subtotal * 1;
        }
        return 0;
    }
    // Add this method inside CartService
    public PriceBreakdown calculatePriceFromCartItems(List<CartItem> cart) {
        double subtotal = cart.stream()
                .mapToDouble(item -> item.getOfferPrice() * item.getQuantity())
                .sum();

        double tax = subtotal * TAX_RATE;
        double shipping = SHIPPING_FEE;
        double serviceFee = SERVICE_FEE;
        double discount = 0; // If no coupon for now, or you can pass coupon as parameter

        double total = subtotal + tax + shipping + serviceFee - discount;
        return new PriceBreakdown(subtotal, tax, shipping, serviceFee, discount, total);
    }

}