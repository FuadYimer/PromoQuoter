package com.promoquoter.PromoQuoter.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItem {
    private UUID productId;
    private int quantity;
}