package com.promoquoter.PromoQuoter.service;


import com.promoquoter.PromoQuoter.domain.model.Promotion;
import com.promoquoter.PromoQuoter.dto.PromotionDto;
import com.promoquoter.PromoQuoter.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public void createPromotions(List<PromotionDto> promotionDtos) {
        List<Promotion> promotions = promotionDtos.stream().map(dto -> {
            Promotion p = new Promotion();
            p.setType(dto.getType());
            p.setPriority(dto.getPriority());
            p.setConfigJson(dto.getConfigJson());
            return p;
        }).collect(Collectors.toList());

        promotionRepository.saveAll(promotions);
    }
}