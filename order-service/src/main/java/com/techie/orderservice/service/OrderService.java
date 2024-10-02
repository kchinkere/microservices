package com.techie.orderservice.service;

import com.techie.orderservice.dto.OrderLineItemsDto;
import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.model.Order;
import com.techie.orderservice.model.OrderLineItem;
import com.techie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        var orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::toOrderLineItems)
                .toList();
        order.setOrderLineItemList(orderLineItems);
        orderRepository.save(order);
    }

    public OrderLineItem toOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItem.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }

}
