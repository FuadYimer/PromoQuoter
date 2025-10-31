package com.promoquoter.PromoQuoter.dto;

import com.promoquoter.PromoQuoter.domain.enums.CustomerSegment;
import com.promoquoter.PromoQuoter.domain.model.CartItem;

import java.util.List;

public class CartRequest {
    private List<CartItem> items;
    private CustomerSegment customerSegment;
}