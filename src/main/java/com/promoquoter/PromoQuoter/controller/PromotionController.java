package com.promoquoter.PromoQuoter.controller;

import com.promoquoter.PromoQuoter.dto.PromotionDto;
import com.promoquoter.PromoQuoter.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<Void> createPromotions(@RequestBody List<PromotionDto> promotions) {
        promotionService.createPromotions(promotions);
        return ResponseEntity.ok().build();
    }
}