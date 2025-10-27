package com.grocart.grocart.Controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    // DTOs for frontend payload
    public record CartItem(
            Long id,
            String product,
            Double originalPrice,
            Double offerPrice,
            String description,
            Integer stock,
            String quantity,      // can ignore
            String imageUrl,
            String category,
            Integer quantityInCart
    ) {}

    public record ShippingInfo(
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String city,
            String province,
            String postalCode,
            Double discount
    ) {}

    public record PaymentRequest(List<CartItem> cart, ShippingInfo shipping) {}

    @PostMapping("/create-checkout-session")
    public PaymentResponse createCheckoutSession(@RequestBody PaymentRequest request) throws StripeException {
        System.out.println("Received payment payload: " + request);

        // Map CartItem to Stripe line items using quantityInCart
        List<SessionCreateParams.LineItem> lineItems = request.cart().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("cad")
                                        .setUnitAmount((long) (item.offerPrice() * 100)) // Stripe expects cents
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.product()) // use product name
                                                        .build()
                                        )
                                        .build()
                        )
                        .setQuantity(item.quantityInCart().longValue()) // use quantityInCart
                        .build()
                )
                .collect(Collectors.toList());

        // Create Stripe checkout session
        SessionCreateParams params = SessionCreateParams.builder()
                .addAllLineItem(lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://grocartinc.ca/payment-success")
                .setCancelUrl("https://grocartinc.ca/payment-failed")
                .build();

        Session session = Session.create(params);

        System.out.println("Created Stripe session URL: " + session.getUrl());

        // Return JSON response
        return new PaymentResponse(session.getUrl());
    }

    // JSON response class
    public record PaymentResponse(String url) {}
}
