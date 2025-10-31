package com.promoquoter.PromoQuoter.service;

import com.promoquoter.PromoQuoter.domain.model.*;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionEngine;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionFactory;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionStrategy;
import com.promoquoter.PromoQuoter.dto.CartRequest;
import com.promoquoter.PromoQuoter.dto.OrderResponse;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;
import com.promoquoter.PromoQuoter.exception.ValidationException;
import com.promoquoter.PromoQuoter.repository.OrderRepository;
import com.promoquoter.PromoQuoter.repository.ProductRepository;
import com.promoquoter.PromoQuoter.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final ProductRepository productRepo;
    private final PromotionRepository promoRepo;
    private final OrderRepository orderRepo;
    private final PromotionFactory promotionFactory;

    public CartService(ProductRepository productRepo, PromotionRepository promoRepo, OrderRepository orderRepo, PromotionFactory promotionFactory) {
        this.productRepo = productRepo;
        this.promoRepo = promoRepo;
        this.orderRepo = orderRepo;
        this.promotionFactory = promotionFactory;
    }

    public QuoteResponse quote(CartRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Cart request must contain at least one item.");
        }

        List<UUID> productIds = request.getItems().stream()
                .map(CartItem::getProductId)
                .toList();

        Map<UUID, Product> productMap = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        //Load promotions and build strategies
        List<Promotion> promotions = promoRepo.findAll();
        List<PromotionStrategy> strategies = promotionFactory.createStrategies(promotions);

        // Apply promotions
        PromotionEngine engine = new PromotionEngine(strategies);
        Quote quote = engine.applyPromotions(request.getItems(), productMap);

        //  Map Quote → QuoteResponse
        QuoteResponse response = new QuoteResponse();
        response.setItems(quote.getItems());
        response.setTotal(quote.getTotal());
        response.setAppliedPromotions(quote.getAppliedPromotions());

        return response;
    }


    @Transactional
    public OrderResponse confirm(CartRequest request, String idempotencyKey) {
        //Validate cart items
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Cart must contain at least one item.");
        }

        // Load products
        List<UUID> productIds = request.getItems().stream()
                .map(CartItem::getProductId)
                .toList();

        Map<UUID, Product> productMap = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        // Step 3: Load promotions and build strategies
        List<Promotion> promotions = promoRepo.findAll();
        List<PromotionStrategy> strategies = promotionFactory.createStrategies(promotions);

        // Apply promotions
        PromotionEngine engine = new PromotionEngine(strategies);
        Quote quote = engine.applyPromotions(request.getItems(), productMap);

        // Create and persist Order
        Order order = new Order();
//        order.setId(UUID.randomUUID());
        order.setItems(mapToOrderItems(quote.getItems()));
        order.setTotal(quote.getTotal());
        order.setAppliedPromotions(quote.getAppliedPromotions());
        order.setCreatedAt(LocalDateTime.now());
        order.setIdempotencyKey(idempotencyKey);

        orderRepo.save(order);

        //Build response DTO
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setItems(quote.getItems());
        response.setTotal(order.getTotal());
        response.setAppliedPromotions(order.getAppliedPromotions());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }

    private List<OrderItem> mapToOrderItems(List<QuoteItem> quoteItems) {
        return quoteItems.stream().map(q -> {
            OrderItem o = new OrderItem();
            o.setProductId(q.getProductId());
            o.setName(q.getName());
            o.setQuantity(q.getQuantity());
            o.setUnitPrice(q.getUnitPrice());
            o.setDiscount(q.getDiscount());
            o.setFinalPrice(q.getFinalPrice());
            return o;
        }).toList();
    }


}