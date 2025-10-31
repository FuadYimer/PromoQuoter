package com.promoquoter.PromoQuoter.domain.model;

import com.promoquoter.PromoQuoter.domain.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Promotion {
    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    private PromotionType type;
    private int priority;
    @Lob
    private String configJson;
}
