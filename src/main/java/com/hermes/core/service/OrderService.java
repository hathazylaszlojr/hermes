package com.hermes.core.service;

import com.hermes.core.domain.ClientOrder;
import com.hermes.core.domain.OrderedProduct;
import com.hermes.core.domain.Product;
import com.hermes.core.dto.OrderDto;
import com.hermes.core.repository.OrderRepository;
import com.hermes.core.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final TimeService timeService;

    public List<ClientOrder> retrieveAllOrdersInTimeInterval(Long startDate, Long endDate) {
        return orderRepository.findAllByTimestampBetween(startDate, endDate);
    }

    private ClientOrder addNewOrder(ClientOrder clientOrder) {
        return orderRepository.save(clientOrder);
    }

    public Double recalculateCurrentOrderValue(Long orderId) {
        return productRepository.findAllById(
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new IllegalArgumentException("Order does not exist"))
                        .getProducts().stream()
                        .map(OrderedProduct::getSku)
                        .collect(Collectors.toList())
        )
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();

    }

    public Double recalculateOrignalOrderValue(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order does not exist"))
                .getProducts().stream()
                .mapToDouble(OrderedProduct::getPrice)
                .sum();

    }

    public ClientOrder addOrderFromDto(OrderDto orderDto) {
        return addNewOrder(
                mapOrderDro(orderDto)
        );
    }

    private ClientOrder mapOrderDro(OrderDto orderDto) {
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setEmail(orderDto.getEmail());
        clientOrder.setTimestamp(timeService.getCurrentUtcEpochMilli());
        List<Product> products = productRepository.findAllById(orderDto.getProductSkus());

        if (products.isEmpty()) {
            throw new IllegalArgumentException("ClientOrder contains invalid product SKUs");
        }

        clientOrder.setProducts(mapProducts(products));
        return clientOrder;
    }

    private List<OrderedProduct> mapProducts(List<Product> products) {
        return products.stream()
                .map(product -> {
                    OrderedProduct op = new OrderedProduct();
                    op.setName(product.getName());
                    op.setPrice(product.getPrice());
                    op.setSku(product.getSku());
                    return op;
                })
                .collect(Collectors.toList());
    }

}
