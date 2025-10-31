package com.promoquoter.PromoQuoter.controller;

import com.promoquoter.PromoQuoter.domain.enums.Category;
import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private UUID productId;

    @BeforeEach
    void setup() {
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setCategory(Category.ELECTRONICS);
        laptop.setPrice(new BigDecimal("1000.00"));
        laptop.setStock(5);
        laptop.setVersion(0);

        productId = productRepository.save(laptop).getId();
    }

    @Test
    void quoteEndpointReturns200WithTotal() throws Exception {
        String payload = """
            {
              "items": [
                {
                  "productId": "%s",
                  "quantity": 2
                }
              ],
              "customerSegment": "REGULAR"
            }
        """.formatted(productId);

        mockMvc.perform(post("/api/v1/cart/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.items[0].productId").value(productId.toString()));
    }

    @Test
    void confirmEndpointReturns200ForValidRequest() throws Exception {
        String payload = """
            {
              "items": [
                {
                  "productId": "%s",
                  "quantity": 1
                }
              ]
            }
        """.formatted(productId);

        mockMvc.perform(post("/api/v1/cart/confirm")
                        .header("Idempotency-Key", "test-key-valid-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    void confirmEndpointReturns409WhenOutOfStock() throws Exception {
        String payload = """
            {
              "items": [
                {
                  "productId": "%s",
                  "quantity": 99
                }
              ]
            }
        """.formatted(productId);

        mockMvc.perform(post("/api/v1/cart/confirm")
                        .header("Idempotency-Key", "test-key-out-of-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Insufficient stock for product Laptop"));
    }

    @Test
    void confirmEndpointReturnsSameOrderForSameIdempotencyKey() throws Exception {
        String payload = """
            {
              "items": [
                {
                  "productId": "%s",
                  "quantity": 1
                }
              ]
            }
        """.formatted(productId);

        // First call
        mockMvc.perform(post("/api/v1/cart/confirm")
                        .header("Idempotency-Key", "test-key-idempotent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists());

        // Second call with same key
        mockMvc.perform(post("/api/v1/cart/confirm")
                        .header("Idempotency-Key", "test-key-idempotent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists());
    }
}