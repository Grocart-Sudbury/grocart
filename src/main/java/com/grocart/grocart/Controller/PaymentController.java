package com.grocart.grocart.Controller;

import com.grocart.grocart.Entities.CartItem;
import com.grocart.grocart.Entities.ShippingInfo;
import com.grocart.grocart.Services.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public record PaymentRequest(List<CartItem> cart, ShippingInfo shipping) {}
    public record PaymentResponse(String url) {}

    @PostMapping("/create-checkout-session")
    public PaymentResponse createCheckoutSession(@RequestBody PaymentRequest request) throws StripeException {
        Session session = paymentService.createCheckoutSession(request);
        return new PaymentResponse(session.getUrl());
    }
}
