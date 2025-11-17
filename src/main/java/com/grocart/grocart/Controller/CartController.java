package com.grocart.grocart.Controller;


import com.grocart.grocart.Entities.CartRequest;
import com.grocart.grocart.Entities.PriceBreakdown;
import com.grocart.grocart.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/pricing")
    public PriceBreakdown calculate(@RequestBody CartRequest request) {
        return cartService.calculatePrice(request);
    }
}