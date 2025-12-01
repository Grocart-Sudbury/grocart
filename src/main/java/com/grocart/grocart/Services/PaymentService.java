package com.grocart.grocart.Services;



import com.grocart.grocart.Controller.PaymentController.PaymentRequest;
import com.grocart.grocart.Entities.CartItem;
import com.grocart.grocart.Entities.PriceBreakdown;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final CartService cartService;


    public PaymentService(CartService cartService) {
        this.cartService = cartService;

    }

    public Session createCheckoutSession(PaymentRequest request) throws StripeException {
        // Calculate total using CartService
        PriceBreakdown price = cartService.calculatePriceFromCartItems(request.cart());

        // Map CartItem to Stripe line items
        List<SessionCreateParams.LineItem> lineItems = request.cart().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("cad")
                                        .setUnitAmount((long) (item.getOfferPrice() * 100)) // Stripe expects cents
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct())
                                                        .build()
                                        )
                                        .build()
                        )
                        .setQuantity(item.getQuantity().longValue())
                        .build()
                )
                .collect(Collectors.toList());

        // Add additional line items for shipping, service fee, tax if needed
        if (price.getShippingCost() > 0) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("cad")
                                            .setUnitAmount((long) (price.getShippingCost() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Shipping Fee")
                                                            .build()
                                            )
                                            .build()
                            )
                            .setQuantity(1L)
                            .build()
            );
        }

        if (price.getServiceFee() > 0) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("cad")
                                            .setUnitAmount((long) (price.getServiceFee() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Service Fee")
                                                            .build()
                                            )
                                            .build()
                            )
                            .setQuantity(1L)
                            .build()
            );
        }
        // Add tax as a separate line item
        if (price.getTax() > 0) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("cad")
                                            .setUnitAmount((long) (price.getTax() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Tax")
                                                            .build()
                                            )
                                            .build()
                            )
                            .setQuantity(1L)
                            .build()
            );
        }

        // Create Stripe checkout session
        SessionCreateParams params = SessionCreateParams.builder()
                .addAllLineItem(lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://grocartinc.ca/payment-success")
                .setCancelUrl("https://grocartinc.ca/payment-failed")
                .build();

        return Session.create(params);
    }
}
