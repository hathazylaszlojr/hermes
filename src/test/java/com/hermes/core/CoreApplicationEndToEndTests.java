package com.hermes.core;

import com.hermes.core.controller.ProductController;
import com.hermes.core.domain.Product;
import com.hermes.core.repository.ProductRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoreApplicationEndToEndTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductController productController;

    @After
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldSaveProductWhenPostedInRestController() {
        Long sku = productController.addNewProduct(
                createProduct("test product", 22.1f)
        );

        assertThat(productController.getAllProducts().size()).isEqualTo(1);

        Optional<Product> savedProductOptional = productRepository.findById(sku);

        assertThat(savedProductOptional.isPresent()).isTrue();
        Product savedProduct = savedProductOptional.get();
        assertThat(savedProduct.getName()).isEqualTo("test product");
        assertThat(savedProduct.getPrice()).isEqualTo(22.1f);
    }

    @Test
    public void shouldUpdateProductWhenPutInRestController() {
        Long sku1 = productController.addNewProduct(
                createProduct("test product 1", 22.1f)
        );

        Long sku2 = productController.addNewProduct(
                createProduct("test product 2", 44.1f)
        );

        Product updatedProduct = productController.updateProduct(sku2, createProduct("new product 3", 17.0f));

        assertThat(productController.getAllProducts().size()).isEqualTo(2);

        Optional<Product> savedProductOptional = productRepository.findById(sku2);

        assertThat(savedProductOptional.isPresent()).isTrue();
        Product savedProduct = savedProductOptional.get();
        assertThat(savedProduct.getName()).isEqualTo(updatedProduct.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(updatedProduct.getPrice());

        Optional<Product> otherProductOptional = productRepository.findById(sku1);

        assertThat(otherProductOptional.isPresent()).isTrue();
        Product otherProduct = otherProductOptional.get();
        assertThat(otherProduct.getName()).isEqualTo("test product 1");
        assertThat(otherProduct.getPrice()).isEqualTo(22.1f);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInvalidUpdateRequestCalled() {
        productController.updateProduct(1L, new Product(2L,"new product 3", 17.0f));
    }

    private Product createProduct(String name, Float price) {
        return new Product(null, name, price);
    }

}
