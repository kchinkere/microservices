package com.techie.orderservice.service;

import com.techie.orderservice.dto.InventoryResponse;
import com.techie.orderservice.dto.OrderLineItemsDto;
import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.model.Order;
import com.techie.orderservice.model.OrderLineItem;
import com.techie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        var orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::toOrderLineItems)
                .toList();
        order.setOrderLineItemList(orderLineItems);

        var skuCodes = orderLineItems.stream().map(OrderLineItem::getSkuCode).toList();

        //call inventory service to check product is in stock
        InventoryResponse[] result = webClient.get().uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();
        if(result == null || result.length == 0) {
            throw new IllegalArgumentException("Products are in stock, please try again later");
        }
        boolean allProductInStock = Arrays.stream(result).allMatch(InventoryResponse::getIsInStock);
        if(!allProductInStock) {
            throw new IllegalArgumentException("Products are in stock, please try again later");
        }
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
