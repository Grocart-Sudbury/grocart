package com.grocart.grocart.Entities;

public class PriceBreakdown {
    private double subtotal;
    private double tax;
    private double shippingCost;
    private double serviceFee;
    private double discount;
    private double total;

    public PriceBreakdown(double subtotal, double tax, double shippingCost, double serviceFee, double discount, double total) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.shippingCost = shippingCost;
        this.serviceFee = serviceFee;
        this.discount = discount;
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
