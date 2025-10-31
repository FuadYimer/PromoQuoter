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
    public Quote apply(List<CartItem> items, Map<UUID, Product> products) {
        List<QuoteItem> quoteItems = new ArrayList<>();

        for (CartItem cartItem : items) {
            if (!cartItem.getProductId().equals(productId)) continue;

            Product product = products.get(productId);
            if (product == null) continue;

            int totalQty = cartItem.getQuantity();
            int eligibleFreeUnits = (totalQty / (buyQty + freeQty)) * freeQty;
            int paidQty = totalQty - eligibleFreeUnits;

            BigDecimal unitPrice = product.getPrice();
            BigDecimal discount = unitPrice.multiply(BigDecimal.valueOf(eligibleFreeUnits));

            QuoteItem quoteItem = new QuoteItem(
                    product.getId(),
                    product.getName(),
                    totalQty,
                    unitPrice,
                    discount
            );

            quoteItems.add(quoteItem);
        }

        Quote quote = new Quote();
        quote.setItems(quoteItems);
        quote.setAppliedPromotions(List.of("BuyXGetYStrategy"));
        return quote;
    }
}

