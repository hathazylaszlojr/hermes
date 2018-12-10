package com.hermes.core.controller;

import com.hermes.core.domain.ClientOrder;
import com.hermes.core.dto.OrderDto;
import com.hermes.core.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<ClientOrder> retrieveAllOrders(
            @RequestParam("startDate") Long startDate,
            @RequestParam("endDate") Long endDate
    ) {

        log.info("Retrieving orders between {} and {}", startDate, endDate);

        return orderService.retrieveAllOrdersInTimeInterval(startDate, endDate);
    }

    @PostMapping
    public ClientOrder addNewOrder(@RequestBody OrderDto orderDto) {
        log.info("Adding new order...");

        Assert.notNull(orderDto.getEmail(), "Email address should be defined.");
        Assert.noNullElements(orderDto.getProductSkus().toArray(), "Ordered product SKUs should not be null");

        return orderService.addOrderFromDto(orderDto);
    }

    @GetMapping("/{orderId}/recalculate")
    public Map<String, Object> recalculateOrderPrice(@PathVariable("orderId") Long orderId) {

        Double newTotal = orderService.recalculateCurrentOrderValue(orderId);
        Double originalTotal = orderService.recalculateOrignalOrderValue(orderId);

        return new HashMap<String, Object>() {
            {
                put("orderId", orderId);
                put("newTotal", newTotal);
                put("originalTotal", originalTotal);
            }
        };
    }

}
