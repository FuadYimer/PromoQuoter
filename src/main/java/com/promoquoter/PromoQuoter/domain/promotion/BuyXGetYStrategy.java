package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BuyXGetYStrategy implements PromotionStrategy {
    private final UUID productId;
    private final int buyQty;
    private final int freeQty;

    public BuyXGetYStrategy(UUID productId, int buyQty, int freeQty) {
        this.productId = productId;
        this.buyQty = buyQty;
        this.freeQty = freeQty;
    }

    @Override
    public QuoteResponse apply(List<CartItem> items, Map<UUID, Product> products) {
        return null;
    }
}

