package com.promoquoter.PromoQuoter.service;

import com.promoquoter.PromoQuoter.dto.CartRequest;
import com.promoquoter.PromoQuoter.dto.OrderResponse;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;
import com.promoquoter.PromoQuoter.repository.OrderRepository;
import com.promoquoter.PromoQuoter.repository.ProductRepository;
import com.promoquoter.PromoQuoter.repository.PromotionRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final ProductRepository productRepo;
    private final PromotionRepository promoRepo;
    private final OrderRepository orderRepo;

    public CartService(ProductRepository productRepo, PromotionRepository promoRepo, OrderRepository orderRepo) {
        this.productRepo = productRepo;
        this.promoRepo = promoRepo;
        this.orderRepo = orderRepo;
    }

    public QuoteResponse quote(CartRequest request) {
        // Load products, apply promotions
        return null;
    }

    public OrderResponse confirm(CartRequest request, String idempotencyKey) {
        // Lock stock, apply promotions, save order
        return null;
    }
}