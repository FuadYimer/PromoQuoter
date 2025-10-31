package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PercentOffCategoryStrategy implements PromotionStrategy {
    private final String category;
    private final BigDecimal percent;

    public PercentOffCategoryStrategy(String category, BigDecimal percent) {
        this.category = category;
        this.percent = percent;
    }

    @Override
    public QuoteResponse apply(List<CartItem> items, Map<UUID, Product> products) {
        return  null;
    }


}