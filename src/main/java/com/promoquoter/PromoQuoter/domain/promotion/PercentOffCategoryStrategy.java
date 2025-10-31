package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.domain.model.Quote;
import com.promoquoter.PromoQuoter.domain.model.QuoteItem;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public Quote apply(List<CartItem> items, Map<UUID, Product> products) {
        List<QuoteItem> discountedItems = new ArrayList<>();

        for (CartItem cartItem : items) {
            Product product = products.get(cartItem.getProductId());
            if (product == null || !product.getCategory().name().equalsIgnoreCase(category)) continue;

            BigDecimal unitPrice = product.getPrice();
            BigDecimal discount = unitPrice
                    .multiply(percent)
                    .divide(BigDecimal.valueOf(100));

            QuoteItem quoteItem = new QuoteItem(
                    product.getId(),
                    product.getName(),
                    cartItem.getQuantity(),
                    unitPrice,
                    discount.multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );

            discountedItems.add(quoteItem);
        }

        Quote quote = new Quote();
        quote.setItems(discountedItems);
        quote.setAppliedPromotions(List.of("PercentOffCategoryStrategy"));
        return quote;
    }


}