package com.promoquoter.PromoQuoter.dto;

import com.promoquoter.PromoQuoter.domain.enums.CustomerSegment;
import com.promoquoter.PromoQuoter.domain.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CartRequest {
    private List<CartItem> items;
    private CustomerSegment customerSegment;
}