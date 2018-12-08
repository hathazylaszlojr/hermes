package com.hermes.core.service;

import com.hermes.core.domain.Product;
import com.hermes.core.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Long addProduct(Product product) {
        return productRepository
                .save(product)
                .getSku();
    }

    public Product updateProduct(Long sku, Product product) {
        product.setSku(sku);
        return productRepository.save(product);
    }
}
