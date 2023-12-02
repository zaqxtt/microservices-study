package com.zaq.orderservice.service;

import com.zaq.orderservice.dto.InventoryResponse;
import com.zaq.orderservice.dto.OrderLineItemsDTO;
import com.zaq.orderservice.dto.OrderRequest;
import com.zaq.orderservice.model.Order;
import com.zaq.orderservice.model.OrderLineItems;
import com.zaq.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToModel)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        //调用库存服务，判断该订单中的商品是否还有库存
        //获取订单中所有商品的skuCode
        List<String> skuCodeList = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode).toList();
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodeList", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("some product is not in stock");
        }
    }

    private OrderLineItems mapToModel(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
