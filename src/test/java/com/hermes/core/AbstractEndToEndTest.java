package com.hermes.core;

import com.hermes.core.domain.Product;
import com.hermes.core.dto.OrderDto;

import java.util.List;

class AbstractEndToEndTest {

    Product createProduct(String name, Double price) {
        return new Product(null, name, price);
    }

    OrderDto createOrderDto(String email, List<Long> productIds) {
        OrderDto orderDto = new OrderDto();
        orderDto.setEmail(email);
        orderDto.setProductSkus(productIds);
        return orderDto;
    }

}
