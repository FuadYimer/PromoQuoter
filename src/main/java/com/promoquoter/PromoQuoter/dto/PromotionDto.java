package com.promoquoter.PromoQuoter.dto;

import com.promoquoter.PromoQuoter.domain.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDto {
    private PromotionType type;
    private int priority;
    private String configJson; // JSON string with rule-specific config
}
