package com.zaq.orderservice.service;

import com.zaq.orderservice.dto.OrderLineItemsDTO;
import com.zaq.orderservice.dto.OrderRequest;
import com.zaq.orderservice.model.Order;
import com.zaq.orderservice.model.OrderLineItems;
import com.zaq.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToModel)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        orderRepository.save(order);
    }

    private OrderLineItems mapToModel(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
