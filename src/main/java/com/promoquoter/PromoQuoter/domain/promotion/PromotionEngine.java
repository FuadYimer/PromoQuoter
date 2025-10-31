package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PromotionEngine {
    private final List<PromotionStrategy> strategies;

    public PromotionEngine(List<PromotionStrategy> strategies) {
        this.strategies = strategies;
    }

    public QuoteResponse applyPromotions(List<CartItem> items, Map<UUID, Product> products) {
        QuoteResponse quote = new QuoteResponse();
        for (PromotionStrategy strategy : strategies) {
            quote = strategy.apply(items, products);
        }
        return quote;
    }
}

