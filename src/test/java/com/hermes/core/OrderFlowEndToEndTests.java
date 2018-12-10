package com.hermes.core;

import com.hermes.core.controller.OrderController;
import com.hermes.core.controller.ProductController;
import com.hermes.core.domain.ClientOrder;
import com.hermes.core.repository.OrderRepository;
import com.hermes.core.repository.ProductRepository;
import com.hermes.core.service.TimeService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderFlowEndToEndTests extends AbstractEndToEndTest {

    public static final double DELTA = 0.001;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductController productController;

    @Autowired
    private OrderController orderController;

    @MockBean
    private TimeService timeService;

    @After
    public void tearDown() {
        productRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @Transactional
    public void shouldReturnAllValidOrdersInTimeInterval() {
        Long id1 = productController.addNewProduct(
                createProduct("Product 1", 12.3d)
        );

        Long id2 = productController.addNewProduct(
                createProduct("Product 2", 22.1d)
        );

        when(timeService.getCurrentUtcEpochMilli()).thenReturn(10L);

        orderController.addNewOrder(
                createOrderDto("secret@example.com", Collections.singletonList(id1))
        );

        when(timeService.getCurrentUtcEpochMilli()).thenReturn(20L);

        orderController.addNewOrder(
                createOrderDto("other@example.com", Collections.singletonList(id2))
        );

        assertThat(orderRepository.findAll().size()).isEqualTo(2);
        assertThat(orderRepository.findAllByTimestampBetween(1L, 9L)).isEmpty();

        assertThat(orderRepository.findAllByTimestampBetween(9L, 10L).size()).isEqualTo(1);
        assertThat(orderRepository.findAllByTimestampBetween(10L, 11L).size()).isEqualTo(1);
        assertThat(orderRepository.findAllByTimestampBetween(10L, 10L).size()).isEqualTo(1);
        ClientOrder savedOrder1 = orderRepository.findAllByTimestampBetween(10L, 10L).get(0);
        assertThat(savedOrder1.getEmail()).isEqualTo("secret@example.com");
        assertThat(savedOrder1.getProducts().size()).isEqualTo(1);
        assertThat(savedOrder1.getProducts().get(0).getName()).isEqualTo("Product 1");

        assertThat(orderRepository.findAllByTimestampBetween(11L, 19L)).isEmpty();
        assertThat(orderRepository.findAllByTimestampBetween(20L, 20L).size()).isEqualTo(1);
        ClientOrder savedOrder2 = orderRepository.findAllByTimestampBetween(20L, 20L).get(0);
        assertThat(savedOrder2.getEmail()).isEqualTo("other@example.com");
        assertThat(savedOrder2.getProducts().size()).isEqualTo(1);
        assertThat(savedOrder2.getProducts().get(0).getName()).isEqualTo("Product 2");

    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateOrderWhenAddingInvalidProductIds() {
        Long id1 = productController.addNewProduct(
                createProduct("Product 1", 12.3d)
        );

        when(timeService.getCurrentUtcEpochMilli()).thenReturn(10L);

        orderController.addNewOrder(
                createOrderDto("secret@example.com", Collections.singletonList(id1 + 1))
        );

    }

    @Test
    @Transactional
    public void shouldRecalculateNewValueWhenProductPriceChanged() {
        Long id1 = productController.addNewProduct(
                createProduct("Product 1", 12.3d)
        );

        Long id2 = productController.addNewProduct(
                createProduct("Product 2", 22.1d)
        );

        when(timeService.getCurrentUtcEpochMilli()).thenReturn(10L);

        ClientOrder savedOrder = orderController.addNewOrder(
                createOrderDto("secret@example.com", Arrays.asList(id1, id2))
        );

        productController.updateProduct(id1, createProduct("Product 1", 27.11d));

        Map<String, Object> result = orderController.recalculateOrderPrice(savedOrder.getId());

        assertThat(result.containsKey("orderId")).isTrue();
        assertThat(result.containsKey("originalTotal")).isTrue();
        assertThat(result.containsKey("newTotal")).isTrue();
        assertThat(result.get("orderId")).isEqualTo(savedOrder.getId());
        assertThat((Double)result.get("originalTotal")).isCloseTo(34.4, within(DELTA));
        assertThat((Double)result.get("newTotal")).isCloseTo(49.21, within(DELTA));

    }

}
