package com.promoquoter.PromoQuoter.repository;

import com.promoquoter.PromoQuoter.domain.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {}
