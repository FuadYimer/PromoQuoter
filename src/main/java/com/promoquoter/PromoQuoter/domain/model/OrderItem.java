package com.promoquoter.PromoQuoter.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class OrderItem {

    private UUID productId;
    private String name;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;

    public OrderItem() {}

    public OrderItem(UUID productId, String name, int quantity, BigDecimal unitPrice, BigDecimal finalPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.finalPrice = finalPrice;
    }
}