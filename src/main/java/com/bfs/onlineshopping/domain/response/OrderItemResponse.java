package com.bfs.onlineshopping.domain.response;

import com.bfs.onlineshopping.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderItemResponse {
    private String productName;
    private double purchasedPrice;
    private int quantity;

    public OrderItemResponse(String productName, double purchasedPrice, int quantity) {
        this.productName = productName;
        this.purchasedPrice = purchasedPrice;
        this.quantity = quantity;
    }

    public static List<OrderItemResponse> fromOrderItems(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new OrderItemResponse(item.getProduct().getName(), item.getPurchasedPrice(), item.getQuantity()))
                .collect(Collectors.toList());
    }
}
