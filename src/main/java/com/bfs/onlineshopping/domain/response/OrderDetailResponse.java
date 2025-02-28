package com.bfs.onlineshopping.domain.response;

import com.bfs.onlineshopping.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null fields from response
public class OrderDetailResponse {
    private LocalDateTime datePlaced;
    private OrderStatus orderStatus;
    private String username; // Included only for admin
    private List<OrderItemResponse> orderItems;

    public OrderDetailResponse(LocalDateTime datePlaced, OrderStatus orderStatus, List<OrderItemResponse> orderItems) {
        this.datePlaced = datePlaced;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
    }
}
