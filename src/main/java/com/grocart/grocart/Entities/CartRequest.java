package com.grocart.grocart.Entities;

import com.grocart.grocart.Controller.PaymentController;

import java.util.List;


public class CartRequest {
    private List<CartItem> cart;
    private String couponCode;

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}