package com.promoquoter.PromoQuoter.domain.promotion;

import com.promoquoter.PromoQuoter.domain.model.CartItem;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.domain.model.Quote;
import com.promoquoter.PromoQuoter.domain.model.QuoteItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PromotionEngine {
    private final List<PromotionStrategy> strategies;

    public PromotionEngine(List<PromotionStrategy> strategies) {
        this.strategies = strategies;
    }

    public Quote applyPromotions(List<CartItem> items, Map<UUID, Product> products) {
        Quote combinedQuote = new Quote();
        List<QuoteItem> allItems = new ArrayList<>();
        List<String> appliedPromotions = new ArrayList<>();

        for (PromotionStrategy strategy : strategies) {
            Quote partial = strategy.apply(items, products);
            allItems.addAll(partial.getItems());
            appliedPromotions.addAll(partial.getAppliedPromotions());
        }

        if (allItems.isEmpty()) {
            for (CartItem cartItem : items) {
                Product product = products.get(cartItem.getProductId());
                if (product == null) continue;

                BigDecimal unitPrice = product.getPrice();
                int qty = cartItem.getQuantity();
                BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(qty));

                QuoteItem item = new QuoteItem(
                        product.getId(),
                        product.getName(),
                        qty,
                        unitPrice,
                        BigDecimal.ZERO
                );
                allItems.add(item);
            }
        }

        BigDecimal total = allItems.stream()
                .map(QuoteItem::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        combinedQuote.setItems(allItems);
        combinedQuote.setTotal(total);
        combinedQuote.setAppliedPromotions(appliedPromotions);

        return combinedQuote;
    }


}

