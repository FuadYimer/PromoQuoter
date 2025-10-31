package com.promoquoter.PromoQuoter.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal total;
    @ElementCollection
    private List<String> appliedPromotions;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String idempotencyKey;
    @Version
    private Long version;
}
