package com.promoquoter.PromoQuoter.controller;

import com.promoquoter.PromoQuoter.dto.ProductDto;
import com.promoquoter.PromoQuoter.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProducts(@RequestBody List<ProductDto> products) {
        System.out.println("Here-=======>");
        productService.createProducts(products);
        return ResponseEntity.ok().build();
    }
}