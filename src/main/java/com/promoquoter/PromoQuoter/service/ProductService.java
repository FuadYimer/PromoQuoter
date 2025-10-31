package com.promoquoter.PromoQuoter.service;

import com.promoquoter.PromoQuoter.domain.model.Product;
import com.promoquoter.PromoQuoter.dto.ProductDto;
import com.promoquoter.PromoQuoter.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProducts(List<ProductDto> productDtos) {
        List<Product> products = productDtos.stream().map(dto -> {
            Product p = new Product();
            p.setName(dto.getName());
            p.setCategory(dto.getCategory());
            p.setPrice(dto.getPrice());
            p.setStock(dto.getStock());
            return p;
        }).collect(Collectors.toList());

        productRepository.saveAll(products);
    }
}