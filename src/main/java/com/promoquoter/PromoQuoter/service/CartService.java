package com.promoquoter.PromoQuoter.service;

import com.promoquoter.PromoQuoter.domain.model.*;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionEngine;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionFactory;
import com.promoquoter.PromoQuoter.domain.promotion.PromotionStrategy;
import com.promoquoter.PromoQuoter.dto.CartRequest;
import com.promoquoter.PromoQuoter.dto.OrderResponse;
import com.promoquoter.PromoQuoter.dto.QuoteResponse;
import com.promoquoter.PromoQuoter.exception.OutOfStockException;
import com.promoquoter.PromoQuoter.exception.ValidationException;
import com.promoquoter.PromoQuoter.repository.OrderRepository;
import com.promoquoter.PromoQuoter.repository.ProductRepository;
import com.promoquoter.PromoQuoter.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final ProductRepository productRepo;
    private final PromotionRepository promoRepo;
    private final OrderRepository orderRepo;
    private final PromotionFactory promotionFactory;

    public CartService(ProductRepository productRepo, PromotionRepository promoRepo,
                       OrderRepository orderRepo, PromotionFactory promotionFactory) {
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

        List<Promotion> promotions = promoRepo.findAll();
        List<PromotionStrategy> strategies = promotionFactory.createStrategies(promotions);

        PromotionEngine engine = new PromotionEngine(strategies);
        Quote quote = engine.applyPromotions(request.getItems(), productMap);

        QuoteResponse response = new QuoteResponse();
        response.setItems(quote.getItems());
        response.setTotal(quote.getTotal());
        response.setAppliedPromotions(quote.getAppliedPromotions());

        return response;
    }

    @Transactional
    public OrderResponse confirm(CartRequest request, String idempotencyKey) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Cart must contain at least one item.");
        }

        if (idempotencyKey != null) {
            Optional<Order> existing = orderRepo.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return mapToResponse(existing.get());
            }
        }
        List<UUID> productIds = request.getItems().stream()
                .map(CartItem::getProductId)
                .toList();

        Map<UUID, Product> productMap = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Promotion> promotions = promoRepo.findAll();
        List<PromotionStrategy> strategies = promotionFactory.createStrategies(promotions);
        PromotionEngine engine = new PromotionEngine(strategies);
        Quote quote = engine.applyPromotions(request.getItems(), productMap);

        for (CartItem item : request.getItems()) {
            UUID productId = item.getProductId();
            int requestedQty = item.getQuantity();

            Product product = productMap.get(productId);
            if (product == null) {
                throw new ValidationException("Product not found: " + productId);
            }

            if (product.getStock() < requestedQty) {
                throw new OutOfStockException("Insufficient stock for product " + product.getName());
            }

            product.setStock(product.getStock() - requestedQty);
            productRepo.save(product);
        }

        Order order = new Order();
        order.setItems(mapToOrderItems(quote.getItems()));
        order.setTotal(quote.getTotal());
        order.setAppliedPromotions(quote.getAppliedPromotions());
        order.setCreatedAt(LocalDateTime.now());
        order.setIdempotencyKey(idempotencyKey);

        orderRepo.save(order);

        return mapToResponse(order);
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

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());

        response.setItems(order.getItems().stream()
                .map(item -> new QuoteItem(
                        item.getProductId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getDiscount()
                ))
                .toList()
        );

        response.setTotal(order.getTotal());
        response.setAppliedPromotions(order.getAppliedPromotions());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }
}
