package com.promoquoter.PromoQuoter.domain.promotion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promoquoter.PromoQuoter.domain.enums.PromotionType;
import com.promoquoter.PromoQuoter.domain.model.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PromotionFactory {

    private final ObjectMapper objectMapper;

    public PromotionStrategy createStrategy(Promotion promotion) {
        try {
            JsonNode config = objectMapper.readTree(promotion.getConfigJson());
            PromotionType type = promotion.getType();

            return switch (type) {
                case PERCENT_OFF_CATEGORY -> new PercentOffCategoryStrategy(
                        config.get("category").asText(),
                        config.get("percent").decimalValue()
                );
                case BUY_X_GET_Y -> new BuyXGetYStrategy(
                        UUID.fromString(config.get("productId").asText()),
                        config.get("buyQty").asInt(),
                        config.get("freeQty").asInt()
                );
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid promotion config: " + e.getMessage(), e);
        }
    }

    public List<PromotionStrategy> createStrategies(List<Promotion> promotions) {
        List<PromotionStrategy> strategies = new ArrayList<>();
        promotions.stream()
                .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
                .forEach(p -> strategies.add(createStrategy(p)));
        return strategies;
    }
}
