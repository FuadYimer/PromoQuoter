package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.domain.model.Quote;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PromotionStrategy {
    Quote apply(List<CartItem> items, Map<UUID, Product> products);
}

