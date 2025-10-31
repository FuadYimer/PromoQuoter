package com.promoquoter.PromoQuoter.dto;

import com.promoquoter.PromoQuoter.domain.model.QuoteItem;

import java.math.BigDecimal;
import java.util.List;

public class QuoteResponse {
    private List<QuoteItem> items;
    private BigDecimal total;
    private List<String> appliedPromotions;
}

