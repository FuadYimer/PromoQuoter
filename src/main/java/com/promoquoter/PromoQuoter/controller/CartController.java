package com.promoquoter.PromoQuoter.controller;

import com.promoquoter.PromoQuoter.dto.CartRequest;
import com.promoquoter.PromoQuoter.dto.OrderResponse;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;
import com.promoquoter.PromoQuoter.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/quote")
    public ResponseEntity<QuoteResponse> quote(@RequestBody @Valid CartRequest request) {
        return ResponseEntity.ok(cartService.quote(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<OrderResponse> confirm(@RequestBody @Valid CartRequest request,
                                                 @RequestHeader(value = "Idempotency-Key", required = false) String key) {
        return ResponseEntity.ok(cartService.confirm(request, key));
    }
}