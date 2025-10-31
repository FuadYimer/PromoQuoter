package com.promoquoter.PromoQuoter.dto;

import com.promoquoter.PromoQuoter.domain.enums.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private String name;
    private Category category;
    private BigDecimal price;
    private int stock;
}
