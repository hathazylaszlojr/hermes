package com.hermes.core.controller;

import com.hermes.core.domain.Product;
import com.hermes.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        log.info("Retrieving all products...");
        return productService.getAllProducts();
    }

    @PostMapping
    public Long addNewProduct(@RequestBody Product product) {
        log.info("Adding new product...");

        Assert.notNull(product.getName(), "Product name should not be null");
        Assert.notNull(product.getPrice(), "Product price should not be null");

        return productService.addProduct(product);
    }

    @PutMapping("/{sku}")
    public Product updateProduct(
            @PathVariable("sku") Long sku,
            @RequestBody Product product
    ) {
        log.info("Updating product...");

        Assert.notNull(product.getName(), "Product name should not be null");
        Assert.notNull(product.getPrice(), "Product price should not be null");

        if (product.getSku() != null && !sku.equals(product.getSku())) {
            throw new IllegalArgumentException("Product SKU can not be changed.");
        }

        return productService.updateProduct(sku, product);
    }

}

