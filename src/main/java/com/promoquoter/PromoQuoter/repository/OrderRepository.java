package com.promoquoter.PromoQuoter.repository;

import com.promoquoter.PromoQuoter.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {}
