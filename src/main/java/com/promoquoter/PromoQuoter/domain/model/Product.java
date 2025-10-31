package com.promoquoter.PromoQuoter.domain.model;

import com.promoquoter.PromoQuoter.domain.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private BigDecimal price;
    private int stock;
    @Version
    private int version;
}